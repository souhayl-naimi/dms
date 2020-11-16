package io.naimi.dms.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double tarif;
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "city")
    private Collection<DeliveryMan> deliveryMen;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "city")
    private Collection<Package> packages;
}
