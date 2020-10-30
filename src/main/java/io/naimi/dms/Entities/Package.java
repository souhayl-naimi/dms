package io.naimi.dms.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SequenceGenerator(name="seq", initialValue=1001, allocationSize=100)
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;
    /*private String city;*/
    private String address;
    private String nameRecipient;
    private Double value;
    private String phoneNumber;
    private boolean toBeOpened;
    private LocalDateTime dateCreated;
    @ManyToOne
    private Vendor vendor;
    @ManyToOne
    private City city;
    @ManyToOne
    private DeliveryMan deliveryMan;
    @OneToMany(mappedBy = "aPackage")
    private List<Comment> comments;
    @OneToMany(mappedBy = "apackage")
    private List<Status> statuses;
}
