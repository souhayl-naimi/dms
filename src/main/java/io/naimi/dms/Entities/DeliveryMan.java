package io.naimi.dms.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeliveryMan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cin;
    private String lastName;
    private String firstName;
    private String password;
    private String phoneNumber;
    private LocalDate dateJoined;
    @OneToMany(mappedBy = "deliveryMan")
    private List<Package> packages;
}
