package tv.logisch.game.worlds;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.*;
import org.json.JSONObject;
import tv.logisch.game.GetDown;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
public class WorldManager {

    private List<WorldObject> worlds;
    private List<ColorObject> colors;

    public WorldManager() {
        File file = new File(Bukkit.getPluginsFolder().getPath()+"/getdown/worlds.json");
        if(!file.exists()) {
            this.worlds = new ArrayList<>();
            this.colors = new ArrayList<>();
            return;
        }
        StringBuilder worldsJson = new StringBuilder();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                worldsJson.append(scanner.nextLine());
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(worldsJson.isEmpty() || !worldsJson.toString().startsWith("{") || !worldsJson.toString().endsWith("}")) {
            this.worlds = new ArrayList<>();
            this.colors = new ArrayList<>();
            return;
        }
        JSONObject json = new JSONObject(worldsJson.toString());
        this.colors = new ArrayList<>();
        if(json.has("colors")) {
            json.getJSONArray("colors").forEach(item -> {
                JSONObject colorJson = (JSONObject) item;
                ColorObject color = new ColorObject(
                        colorJson.getString("name"),
                        colorJson.getJSONArray("materials").toList().stream()
                                .map(obj -> Material.getMaterial((String) obj))
                                .filter(Objects::nonNull)
                                .toList(),
                        Material.getMaterial(colorJson.getString("floor"))
                );
                this.colors.add(color);
            });
        }
        this.worlds = new ArrayList<>();
        if(json.has("worlds")) {
            json.getJSONArray("worlds").forEach(item -> {
                JSONObject worldJson = (JSONObject) item;

                String name = worldJson.getString("name");
                String difficulty = worldJson.getString("difficulty");
                List<String> colors = worldJson.getJSONArray("colors").toList().stream()
                        .map(Object::toString)
                        .toList();
                JSONObject spawnPointJson = worldJson.getJSONObject("spawnPoint");
                Location spawnPoint = new Location(
                        Bukkit.getWorld(name),
                        spawnPointJson.getDouble("x"),
                        spawnPointJson.getDouble("y"),
                        spawnPointJson.getDouble("z"),
                        (float) spawnPointJson.getDouble("yaw"),
                        (float) spawnPointJson.getDouble("pitch")
                );
                List<String> builders = worldJson.getJSONArray("builders").toList().stream()
                        .map(Object::toString)
                        .toList();
                JSONObject materialsJson = worldJson.getJSONObject("materials");
                Map<Material, Double> materials = materialsJson.toMap().entrySet().stream()
                        .collect(Collectors.toMap(
                                entry -> Material.getMaterial(entry.getKey()),
                                entry -> materialsJson.getDouble(entry.getKey())
                        ));
                JSONObject loc1Json = worldJson.getJSONObject("loc1");
                Location loc1 = new Location(
                        Bukkit.getWorld(name),
                        loc1Json.getDouble("x"),
                        loc1Json.getDouble("y"),
                        loc1Json.getDouble("z")
                );
                JSONObject loc2Json = worldJson.getJSONObject("loc2");
                Location loc2 = new Location(
                        Bukkit.getWorld(name),
                        loc2Json.getDouble("x"),
                        loc2Json.getDouble("y"),
                        loc2Json.getDouble("z")
                );
                Material placeholder = Material.getMaterial(worldJson.getString("placeholder"));
                Material floorPlaceholder = Material.getMaterial(worldJson.getString("floorPlaceholder"));
                WorldObject world = new WorldObject(
                        name,
                        difficulty,
                        colors,
                        spawnPoint,
                        builders,
                        materials,
                        loc1,
                        loc2,
                        placeholder,
                        floorPlaceholder
                );
                this.worlds.add(world);
            });
        }
    }

    public CompletableFuture<WorldObject> generateWorld(String name) {
        GetDown.instance().logger().info("Generating world: " + name);
        CompletableFuture<WorldObject> future = new CompletableFuture<>();

        // Hole das WorldObject (z.B. aus Konfiguration)
        WorldObject worldObject = this.worlds.stream()
                .filter(world -> world.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        if (worldObject == null) {
            future.completeExceptionally(new IllegalArgumentException("World not found: " + name));
            return future;
        }

        // World-Erstellung MUSS synchron passieren
        Bukkit.getScheduler().runTask(GetDown.instance(), () -> {
            World world = Bukkit.createWorld(new WorldCreator(name));

            if (world == null) {
                future.completeExceptionally(new IllegalStateException("World creation failed: " + name));
                return;
            }

            GetDown.instance().logger().info("World created: " + world.getName());

            // Initiale Einstellungen (synchron)
            world.setSpawnLocation(worldObject.spawnPoint().getBlockX(), worldObject.spawnPoint().getBlockY(), worldObject.spawnPoint().getBlockZ());
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);

            // Dann asynchron weitermachen (Blockersetzung etc.)
            Bukkit.getScheduler().runTaskAsynchronously(GetDown.instance(), () -> {
                try {
                    // Zufällige Farbe
                    String colorName = worldObject.colors().stream()
                            .skip((int) (Math.random() * worldObject.colors().size()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("No color found for world: " + name));

                    ColorObject colorObject = this.colors.stream()
                            .filter(color -> color.name().equalsIgnoreCase(colorName))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Color not found: " + colorName));

                    List<Location> placeholderLocations = new ArrayList<>();

                    int coun2t = 0;
                    // Block-Ersetzung vorbereiten
                    int x1 = worldObject.loc1().getBlockX();
                    int x2 = worldObject.loc2().getBlockX();
                    if( x1 > x2) {
                        int temp = x1;
                        x1 = x2;
                        x2 = temp;
                    }
                    int y1 = worldObject.loc1().getBlockY();
                    int y2 = worldObject.loc2().getBlockY();
                    if( y1 > y2) {
                        int temp = y1;
                        y1 = y2;
                        y2 = temp;
                    }
                    int z1 = worldObject.loc1().getBlockZ();
                    int z2 = worldObject.loc2().getBlockZ();
                    if( z1 > z2) {
                        int temp = z1;
                        z1 = z2;
                        z2 = temp;
                    }
                    GetDown.instance().logger().info("Replacing blocks in area: " + x1 + "," + y1 + "," + z1 + " to " + x2 + "," + y2 + "," + z2);
                    GetDown.instance().logger().info("Placeholder: " + worldObject.placeholder() + ", Floor Placeholder: " + worldObject.floorPlaceholder());
                    for (int x = x1; x <= x2; x++) {
                        for (int y = y1; y <= y2; y++) {
                            for (int z = z1; z <= z2; z++) {
                                coun2t++;
                                Location loc = new Location(world, x, y, z);
                                Material type = world.getBlockAt(loc).getType();

                                if (type == worldObject.placeholder()) {
                                    placeholderLocations.add(loc);
                                } else if (type == worldObject.floorPlaceholder()) {
                                    Bukkit.getScheduler().runTask(GetDown.instance(), () -> {
                                        world.getBlockAt(loc).setType(colorObject.floor());
                                    });
                                }
                            }
                        }
                    }
                    GetDown.instance().logger().info("Total placeholder locations: " + placeholderLocations.size() + " in " + coun2t + " blocks");

                    int total = placeholderLocations.size();
                    GetDown.instance().logger().info("Total placeholder blocks: " + total);
                    for (Map.Entry<Material, Double> entry : worldObject.materials().entrySet()) {
                        int count = (int) (total * (entry.getValue() / 100.0));
                        GetDown.instance().logger().info("Replacing " + count + " blocks with " + entry.getKey() + " (" + entry.getValue() + "%)");
                        Collections.shuffle(placeholderLocations);
                        for (int i = 0; i < count && !placeholderLocations.isEmpty(); i++) {
                            Location loc = placeholderLocations.removeFirst();
                            Bukkit.getScheduler().runTask(GetDown.instance(), () -> {
                                world.getBlockAt(loc).setType(entry.getKey());
                            });
                        }
                    }
                    GetDown.instance().logger().info("Remaining placeholder locations: " + placeholderLocations.size());

                    for (Location loc : placeholderLocations) {
                        Material material = colorObject.materials().get((int) (Math.random() * colorObject.materials().size()));
                        Bukkit.getScheduler().runTask(GetDown.instance(), () -> {
                            world.getBlockAt(loc).setType(material);
                        });
                    }

                    // ✅ Welt ist fertig
                    future.complete(worldObject);
                    GetDown.instance().logger().info("Finished world generation: " + name);

                } catch (Exception e) {
                    future.completeExceptionally(new RuntimeException("Error generating world: " + name, e));
                }
            });
        });

        return future;
    }




}
