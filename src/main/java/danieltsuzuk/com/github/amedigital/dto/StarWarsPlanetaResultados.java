package danieltsuzuk.com.github.amedigital.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class StarWarsPlanetaResultados {
    public String name;
    public String rotation_period;
    public String orbital_period;
    public String diameter;
    public String climate;
    public String gravity;
    public String terrain;
    public String surface_water;
    public String population;
    public ArrayList<String> residents;
    public ArrayList<String> films;
    public Date created;
    public Date edited;
    public String url;
}
