package com.adrar.videogame.repository;

import com.adrar.videogame.database.Mysql;
import com.adrar.videogame.entity.Device;

import java.sql.*;
import java.util.ArrayList;

public class DeviceRepository {
    //Attribut
    private final Connection connection;

    //Constructeur
    public DeviceRepository() {
        this.connection = Mysql.getConnect();
    }

    //Méthodes
    public Device find(Integer id)
    {
        Device device = null;
        try {
            //1 écrire la requête
            String sql = "SELECT id, name, type, manufacturer FROM device WHERE id = ?";
            //2 preparer la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //3 assigner un paramètre
            preparedStatement.setInt(1, id);
            //4 éxécuter la requête
            ResultSet resultSet = preparedStatement.executeQuery();
            //5 retourner la requête
            //Test si le device existe
            if (resultSet.next())
            {
                //Assigner un objet à device
                device = new Device(
                        resultSet.getString("name"),
                        resultSet.getString("type"),
                        resultSet.getString("manufacturer"));
                device.setId(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return device;
    }

    public ArrayList<Device> findAll()
    {
        ArrayList<Device> devices = new ArrayList<>();
        try {
            //1 écrire la requête SQL
            String sql = "SELECT id, name, type, manufacturer FROM device";
            //2 préparer la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //3 éxécuter la requête
            ResultSet resultSet = preparedStatement.executeQuery();
            //4 retourner le resultat
            while (resultSet.next()) {
                //Créer un objet Device
                Device device = new Device(
                        resultSet.getString("name"),
                        resultSet.getString("type"),
                        resultSet.getString("manufacturer")
                );
                device.setId(resultSet.getInt("id"));
                //ajouter à notre arrayList
                devices.add(device);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //Retourner l'ArrayList
        if (devices.isEmpty()) {
            System.out.println("La liste des device est vide");
        }
        return devices;
    }

    public Device findOneByName(String name)
    {
        return null;
    }

    public boolean isDeviceExistsByName(String name) {
        return  true;
    }

    public Device saveDevice(Device device)
    {
        try {
            //1 écrire la requête SQL
            String sql = "INSERT INTO device(name, type, manufacturer) VALUE (?, ?, ?)";
            //2 préparer la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            //3 Assigner préparer la requête
            preparedStatement.setString(1,device.getName());
            preparedStatement.setString(2,device.getType());
            preparedStatement.setString(3,device.getManufacturer());
            //4 Exécuter la requête
            int row = preparedStatement.executeUpdate();
            //test si la requête est passée
            if (row > 0) {
                //Récupérer le resultat de la requête (valeur de la clé primaire)
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                //Véfifier si on à un resultat
                if (resultSet.next()) {
                    //Setter l'id à l'objet
                    device.setId(resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return device;
    }

    public Device updateDevice(Device device)
    {
        return null;
    }

    public void delete(Integer id) {}
}
