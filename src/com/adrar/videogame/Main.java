package com.adrar.videogame;

import com.adrar.videogame.entity.Device;
import com.adrar.videogame.entity.Game;
import com.adrar.videogame.repository.DeviceRepository;
import com.adrar.videogame.repository.GameRepository;

import java.sql.Date;
import java.util.ArrayList;

public class Main {

    static void main() {
        DeviceRepository deviceRepository = new DeviceRepository();

        GameRepository gameRepository = new GameRepository();
        Game game = new Game("Sims 3", new Date(2020, 02, 05),"RPG");
/*        Device nSwitch = new Device();
        Device xbox = new Device();
        Device xbox360 = new Device();
        nSwitch.setId(1);
        xbox.setId(2);
        xbox360.setId(3);
        game.addDevice(xbox);
        game.addDevice(xbox360);
        System.out.println(gameRepository.save(game));*/
/*        Game recup = gameRepository.find(2);
        Device play = new Device();
        //Device
        play.setId(5);
        Device device = deviceRepository.find(2);
        recup.setTitle("Darksouls");
        ArrayList<Device> devices = new ArrayList<>();
        devices.add(device);
        devices.add(play);
        recup.setDevices(devices);
        System.out.println(recup);
        Game apres = gameRepository.update(recup);
        System.out.println(apres);*/
        //System.out.println(gameRepository.findAll());
        //Supprimer le jeu id 5
        gameRepository.delete(5);

    }
}
