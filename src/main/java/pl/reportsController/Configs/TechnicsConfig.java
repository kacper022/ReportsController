package pl.reportsController.Configs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TechnicsConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long id_technic;
    private String colorData;

    public TechnicsConfig(long id_technic, String colorData){
        this.id_technic = id_technic;
        this.colorData = colorData;
    }
}
