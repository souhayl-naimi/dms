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
import java.util.ArrayList;
import java.util.Collection;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Vendor {
    @Id
    @NotEmpty(message = "remplissez ce champ vide.")
    private String cin;
    @NotEmpty (message = "remplissez ce champ vide")
    private String fullName;
    private String password;
    @NotNull (message = "remplissez ce champ vide.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    @Email (message = "E-mail invalide.")
    @NotEmpty (message = "remplissez ce champ vide.")
    private String email;
    @NotEmpty (message = "remplissez ce champ vide.")
    @Size(max = 10,message = "Numéro de téléphone invalide.")
    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateJoined;
    @OneToMany(mappedBy = "vendor")
    private List<Package> packages;
}