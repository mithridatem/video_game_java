package com.adrar.videogame.repository;

import com.adrar.videogame.database.Mysql;
import com.adrar.videogame.entity.Device;
import com.adrar.videogame.entity.Game;
import com.mysql.cj.MysqlConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.PropertyResourceBundle;

public class GameRepository implements  RepositoryInterface<Game>
{
    private Connection connection;

    public GameRepository()
    {
        this.connection = Mysql.getConnect();
    }

    //Version jointures
    public Game find2(Integer id) {
        Game game = null;
        try {
            String sql = "SELECT game.id, title, game.type, published_at, GROUP_CONCAT(device.name) device_names, \n" +
                    "GROUP_CONCAT(device.type) device_types, GROUP_CONCAT(device.id) device_ids,\n" +
                    "GROUP_CONCAT(device.manufacturer) device_manufacturers    from game\n" +
                    "LEFT JOIN game_device ON game.id = game_device.id_game \n" +
                    "LEFT JOIN device ON game_device.id_device = device.id WHERE game.id = ? GROUP BY game.id";
            //Préparation de la requête
            PreparedStatement ps = connection.prepareStatement(sql);
            //Assignation du paramètre
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            //Si il existe
            if (rs.next())
            {
                game = new Game();
                game.setId(rs.getInt("id"));
                game.setTitle(rs.getString("title"));
                game.setType(rs.getString("type"));
                game.setPublishedAt(rs.getDate("published_at"));
                //Test si le jeu est associé à un ou plusieurs device
                if (rs.getString("device_manufacturers") != null && !rs.getString("device_manufacturers").isBlank()) {
                    String [] manufacturers = rs.getString("device_manufacturers").split(",");
                    String [] types = rs.getString("device_types").split(",");
                    String [] names = rs.getString("device_names").split(",");
                    String [] ids = rs.getString("device_ids").split(",");
                    for(int i = 0; i < manufacturers.length; i++) {
                        Device device = new Device(
                                names[i],
                                types[i],
                                manufacturers[i]
                        );
                        device.setId(Integer.parseInt(ids[i]));
                        game.addDevice(device);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return game;
    }

    @Override
    public Game find(Integer id) {
        Game game = null;
        try {
            String sql = "SELECT game.id, title, game.type, published_at FROM game WHERE id = ?";
            //Préparation de la requête
            PreparedStatement ps = connection.prepareStatement(sql);
            //Assignation du paramètre
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            //Si il existe
            if (rs.next())
            {
                game = new Game();
                game.setId(rs.getInt("id"));
                game.setTitle(rs.getString("title"));
                game.setType(rs.getString("type"));
                game.setPublishedAt(rs.getDate("published_at"));
                //Association des devices
                ArrayList<Device> devices = findAllDevice(id);
                game.setDevices(devices);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return game;
    }

    //Version découpée
    @Override
    public ArrayList<Game> findAll() {
        ArrayList<Game> games = new ArrayList<>();
        try {
            String sql = "SELECT game.id, title, game.type, published_at FROM game";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Game game = new Game();
                game.setId(rs.getInt("id"));
                game.setTitle(rs.getString("title"));
                game.setType(rs.getString("type"));
                game.setPublishedAt(rs.getDate("published_at"));
                //Association des devices
                ArrayList<Device> devices = findAllDevice(game.getId());
                game.setDevices(devices);
                games.add(game);
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
        return games;
    }
    //Version avec jointures
    public ArrayList<Game> findAll2() {
        ArrayList<Game> games = new ArrayList<>();
        try {
            String sql = "SELECT game.id, title, game.type, published_at, GROUP_CONCAT(device.name) device_names, \n" +
                    "GROUP_CONCAT(device.type) device_types, GROUP_CONCAT(device.id) device_ids,\n" +
                    "GROUP_CONCAT(device.manufacturer) device_manufacturers    from game\n" +
                    "LEFT JOIN game_device ON game.id = game_device.id_game \n" +
                    "LEFT JOIN device ON game_device.id_device = device.id GROUP BY game.id";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Game game = new Game();
                game.setId(rs.getInt("id"));
                game.setTitle(rs.getString("title"));
                game.setType(rs.getString("type"));
                game.setPublishedAt(rs.getDate("published_at"));
                if (rs.getString("device_manufacturers") != null && !rs.getString("device_manufacturers").isBlank()) {
                    String [] manufacturers = rs.getString("device_manufacturers").split(",");
                    String [] types = rs.getString("device_types").split(",");
                    String [] names = rs.getString("device_names").split(",");
                    String [] ids = rs.getString("device_ids").split(",");
                    for(int i = 0; i < manufacturers.length; i++) {
                        Device device = new Device(
                                names[i],
                                types[i],
                                manufacturers[i]
                        );
                        device.setId(Integer.parseInt(ids[i]));
                        game.addDevice(device);
                    }
                }
                games.add(game);
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
        return games;
    }

    @Override
    public Game save(Game game) {
        try{
            String sql = "INSERT INTO game(title, type, published_at) VALUES(?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql,  Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, game.getTitle());
            ps.setString(2, game.getType());
            ps.setDate(3, new java.sql.Date(game.getPublishedAt().getTime()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int idGame = 0;
            if (rs.next()) {
                game.setId(rs.getInt(1));
                //Insert dans la table d'association game_device
                for(Device device : game.getDevices()) {
                    String sql2 = "INSERT INTO game_device (id_game, id_device) VALUES(?, ?)";
                    PreparedStatement ps2 = connection.prepareStatement(sql2);
                    ps2.setInt(1, game.getId());
                    ps2.setInt(2, device.getId());
                    ps2.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return game;
    }

    @Override
    public Game update(Game game) {

        //Ecrire une requête de MAJ update -> game , title, date, type
        try {
            String sqlUpdate = "UPDATE game SET title = ?, type = ?, published_at = ? WHERE id = ?";
            //Préparer la requête
            PreparedStatement ps = connection.prepareStatement(sqlUpdate);
            //Assigner 4 les paramètres
            ps.setString(1, game.getTitle());
            ps.setString(2, game.getType());
            ps.setDate(3, game.getPublishedAt());
            ps.setInt(4, game.getId());
            //Exécuter la requête
            ps.executeUpdate();
            //Ecrire une requête de MAJ delete game_device (tous les enregistrements ou id_game existe)
            deleteGameDevice(game);
            //Ecrire plusieurs requête de MAJ insert -> game_device
            saveDeviceToGame(game);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return game;
    }

    @Override
    public void delete(Integer id) {
        try {
            Game game = new Game();
            game.setId(id);
            //Supprimer dans la table association game_device
            deleteGameDevice(game);
            //Créer la requête de suppression
            String sqlDelete = "DELETE FROM game WHERE id = ?";
            //préparer la requête de suppression
            PreparedStatement ps = connection.prepareStatement(sqlDelete);
            //Assigner le paramètre id
            ps.setInt(1, id);
            //Exécuter la requête
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteGameDevice(Game game)
    {
        try {
            //Ecrire la requête de suppression
            String sql = "DELETE FROM game_device WHERE id_game = ?";
            //Préparer la requête
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, game.getId());
            //Exécution de la requête
            ps.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveDeviceToGame(Game game)
    {
        try {
            for(Device device : game.getDevices())
            {
                String sql = "INSERT INTO game_device (id_game, id_device) VALUES(?, ?)";
                //Préparer la requête
                PreparedStatement ps = connection.prepareStatement(sql);
                //Assigner les paramètres
                ps.setInt(1, game.getId());
                ps.setInt(2, device.getId());
                //Exécuter la requête
                ps.executeUpdate();
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Device> findAllDevice(Integer id)
    {
        ArrayList<Device> devices = new ArrayList<>();
        try {
            String sql = "SELECT device.id, name, type, manufacturer FROM game_device " +
                    "INNER JOIN device ON id_device = device.id WHERE id_game = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                Device device = new Device();
                device.setId(rs.getInt("id"));
                device.setName(rs.getString("name"));
                device.setType(rs.getString("type"));
                device.setManufacturer(rs.getString("manufacturer"));
                devices.add(device);
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
        return devices;
    }
}
