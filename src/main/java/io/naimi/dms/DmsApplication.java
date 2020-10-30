package io.naimi.dms;

import com.sun.xml.bind.v2.runtime.reflect.Lister;
import io.naimi.dms.DAO.PackageRepository;
import io.naimi.dms.DAO.RoleRepository;
import io.naimi.dms.DAO.UserRepository;
import io.naimi.dms.DAO.VendorRepository;
import io.naimi.dms.Entities.*;
import io.naimi.dms.Entities.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootApplication
public class DmsApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DmsApplication.class, args);
    }
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public void run(String... args) throws Exception {

        Vendor vendor = new Vendor();
        vendor.setFullName("Souhayl Naimi");
        vendor.setCin("MD13");
        vendor.setDateJoined(LocalDate.now());
        vendor.setEmail("souhayl.naimi@outlook.com");
        vendor.setPhone("0664124515");
        vendor.setDob(LocalDate.of(1996,05,23));
        vendorRepository.save(vendor);




        Role role = new Role("ROLE_VENDOR", "role Vendeur");
        roleRepository.save(role);
        List<Role> roles = new ArrayList<Role>();
        roles.add(role);

        User user = new User();
        user.setCin("MD13");
        user.setPassword("$2a$10$SAdK.T/kr440mqW7UzE7i.JKmn79INS3eUtgN0gOBHD3Wogxa679u");
        user.setEnabled(true);
        user.setRoles(roles);

        userRepository.save(user);
    }
}
