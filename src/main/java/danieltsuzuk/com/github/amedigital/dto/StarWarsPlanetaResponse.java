package danieltsuzuk.com.github.amedigital.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class StarWarsPlanetaResponse {
    public int count;
    public Object next;
    public Object previous;
    public ArrayList<StarWarsPlanetaResultados> results;
}
