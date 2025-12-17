package com.adrar.videogame.repository;

import com.adrar.videogame.database.Mysql;
import com.adrar.videogame.entity.Device;

import java.sql.*;
import java.util.ArrayList;

public class DeviceRepository implements RepositoryInterface<Device>{
    //Attribut
    private final Connection connection;

    //Constructeur
    public DeviceRepository() {
        this.connection = Mysql.getConnect();
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
        Device device = null;
        try{
            String sql = "SELECT id, name, type, manufacturer FROM device WHERE name = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                device = new Device(
                  rs.getString("name"),
                  rs.getString("type"),
                  rs.getString("manufacturer")
                );
                device.setId(rs.getInt("id"));
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
        return device;
    }

    public boolean isDeviceExistsByName(String name){
        Boolean result = false;
        try{
            String sql = "SELECT id, name, type, manufacturer FROM device WHERE name = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            result = rs.next();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return result;
    }

    public Device save(Device device)
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
                    device.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return device;
    }

    public Device update(Device device)
    {
        try {
            String sql = "UPDATE device SET name = ?, type = ?, manufacturer = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, device.getName());
            ps.setString(2, device.getType());
            ps.setString(3, device.getManufacturer());
            ps.setInt(4, device.getId());
            ps.executeUpdate();
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
        return device;
    }

    public void delete(Integer id)
    {
        try {
            String sql = "DELETE FROM device WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            int row = ps.executeUpdate();
            if (row < 1)
            {
                System.out.println("Le device n'existe pas");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Device find(Integer id) {
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
}
