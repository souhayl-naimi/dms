package io.naimi.dms.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SequenceGenerator(name="seq", initialValue=3201, allocationSize=100)
public class DeliveryMan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    private String cin;
    private String username;
    private String fullName;

    private String rib;

    private String password;

    private String phone;

    private String email;

    @ManyToOne
    private City city;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateJoined;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "deliveryMan")
    private List<Package> packages;
}
