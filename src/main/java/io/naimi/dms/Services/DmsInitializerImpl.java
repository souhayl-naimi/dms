package io.naimi.dms.Services;

import io.naimi.dms.DAO.*;
import io.naimi.dms.Entities.*;
import io.naimi.dms.Entities.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@Transactional
public class DmsInitializerImpl implements IDmsInitializer {
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private StatusRepository statusRepository;

    @Override
    public void initVendors() {

        Vendor vendor = new Vendor();
        vendor.setFullName("Souhayl Naimi");
        vendor.setDob(LocalDate.of(1996, 05, 23));
        vendor.setPhone("0664124515");
        vendor.setEmail("souhayl.naimi@outlook.com");
        vendor.setDateJoined(LocalDate.now());
        vendor.setCin("MD13");
        vendor.setRib("FR7620041010057643834595126");
        vendor.setPassword("$2a$10$SAdK.T/kr440mqW7UzE7i.JKmn79INS3eUtgN0gOBHD3Wogxa679u");
        vendorRepository.save(vendor);

        Vendor vendor1 = new Vendor();
        vendor1.setFullName("Soukaina Mabchour");
        vendor1.setDob(LocalDate.of(1998, 12, 04));
        vendor1.setPhone("0660285774");
        vendor1.setEmail("soukaina.mabchour@outlook.com");
        vendor1.setDateJoined(LocalDate.now());
        vendor1.setCin("BK12984");
        vendor1.setPassword("$2a$10$SAdK.T/kr440mqW7UzE7i.JKmn79INS3eUtgN0gOBHD3Wogxa679u");
        vendor1.setRib("FR7620041010057643834595126");
        vendorRepository.save(vendor1);

        Vendor vendor2 = new Vendor();
        vendor2.setFullName("Mina Mekkaoui");
        vendor2.setDob(LocalDate.of(1960, 1, 1));
        vendor2.setPhone("0690909231");
        vendor2.setEmail("mina.mekkaoui@outlook.com");
        vendor2.setDateJoined(LocalDate.now());
        vendor2.setCin("M7163");
        vendor2.setRib("FR7620041010057643834595126");
        vendor2.setPassword("$2a$10$SAdK.T/kr440mqW7UzE7i.JKmn79INS3eUtgN0gOBHD3Wogxa679u");
        vendorRepository.save(vendor2);

    }

    @Override
    public void initUsers() {
        Role role = new Role("ROLE_VENDOR", "role Vendeur");
        roleRepository.save(role);
        List<Role> roles = new ArrayList<Role>();
        roles.add(role);
        vendorRepository.findAll().forEach(vendor -> {
            User user = new User();
            user.setUsername("G" + vendor.getId());
            user.setEnabled(true);
            user.setPassword("$2a$10$SAdK.T/kr440mqW7UzE7i.JKmn79INS3eUtgN0gOBHD3Wogxa679u");
            user.setRoles(roles);
            userRepository.save(user);
        });

        Role roleAdmin = new Role("ROLE_ADMIN", "role Vendeur");
        roleRepository.save(roleAdmin);
        List<Role> rolesAdmin = new ArrayList<Role>();
        rolesAdmin.add(roleAdmin);

        User user1 = new User();
        user1.setUsername("admin");
        user1.setEnabled(true);
        user1.setPassword("$2a$10$SAdK.T/kr440mqW7UzE7i.JKmn79INS3eUtgN0gOBHD3Wogxa679u");
        user1.setRoles(rolesAdmin);
        userRepository.save(user1);
    }

    @Override
    public void initCities() {
        Stream.of("Marrakesh","Casablance","El Jadida","Rabat","Safi","Khouribga").forEach(name->{
            City city = new City();
            city.setName(name);
            city.setTarif(new Random().nextInt(50));
            cityRepository.save(city);
        });
    }
    @Override
    public void initPackages() {
        vendorRepository.findAll().forEach(vendor -> {
            Package aPackage = new Package();
            aPackage.setReference("REF1");
            aPackage.setVendor(vendor);
            aPackage.setDateCreated(LocalDateTime.now());
            aPackage.setAddress("Rue Jeanne Rcamier 76");
            aPackage.setNameRecipient("Laure Sauvage");
            aPackage.setPhoneNumber("0922192543");
            aPackage.setToBeOpened(false);
            aPackage.setValue(199.99);
            aPackage.setCity(cityRepository.findById(Long.valueOf(3)).get());

            Status status = new Status();
            status.setStatus("livraison demandé");
            status.setTimeUpdated(LocalDateTime.now());
            status.setApackage(aPackage);
            List<Status> statuses = new ArrayList<>();
            statuses.add(status);
            aPackage.setStatuses(statuses);

            Comment comment = new Comment();
            comment.setComment("firstcomment");
            comment.setTimeCommented(LocalDateTime.now());
            comment.setAPackage(aPackage);
            List<Comment> comments = new ArrayList<Comment>();
            comments.add(comment);
            aPackage.setComments(comments);

            packageRepository.save(aPackage);
            commentRepository.save(comment);
            statusRepository.save(status);

//            ************************

            Package aPackage1 = new Package();
            aPackage1.setReference("REF2");
            aPackage1.setVendor(vendor);
            aPackage1.setDateCreated(LocalDateTime.now());
            aPackage1.setAddress("Rue Klber 7c");
            aPackage1.setNameRecipient("Joseph Chauvin");
            aPackage1.setPhoneNumber("0825078633");
            aPackage1.setToBeOpened(false);
            aPackage1.setValue(99.99);
            aPackage1.setCity(cityRepository.findById(Long.valueOf(2)).get());

            Status status1 = new Status();
            status1.setStatus("livraison demandé");
            status1.setTimeUpdated(LocalDateTime.now());
            status1.setApackage(aPackage1);
            List<Status> statuses1 = new ArrayList<>();
            statuses1.add(status1);
            aPackage1.setStatuses(statuses1);

            Comment comment1 = new Comment();
            comment1.setComment("firstcomment");
            comment1.setTimeCommented(LocalDateTime.now());
            comment1.setAPackage(aPackage1);
            List<Comment> comments1 = new ArrayList<Comment>();
            comments1.add(comment1);
            aPackage1.setComments(comments1);

            packageRepository.save(aPackage1);
            commentRepository.save(comment1);
            statusRepository.save(status1);


            Package aPackage2 = new Package();
            aPackage2.setReference("REF3");
            aPackage2.setVendor(vendor);
            aPackage2.setDateCreated(LocalDateTime.now());
            aPackage2.setAddress("Rue de la Rpublique 195c");
            aPackage2.setNameRecipient("Charlotte Lecomte");
            aPackage2.setPhoneNumber("0929849575");
            aPackage2.setToBeOpened(true);
            aPackage2.setValue(249.99);
            aPackage2.setCity(cityRepository.findById(Long.valueOf(1)).get());

            Status status2 = new Status();
            status2.setStatus("livraison demandé");
            status2.setTimeUpdated(LocalDateTime.now());
            status2.setApackage(aPackage2);
            List<Status> statuses2 = new ArrayList<>();
            statuses2.add(status2);
            aPackage2.setStatuses(statuses2);

            Comment comment2 = new Comment();
            comment2.setComment("firstcomment");
            comment2.setTimeCommented(LocalDateTime.now());
            comment2.setAPackage(aPackage2);
            List<Comment> comments2 = new ArrayList<Comment>();
            comments2.add(comment2);
            aPackage2.setComments(comments2);

            packageRepository.save(aPackage2);
            commentRepository.save(comment2);
            statusRepository.save(status2);

        });
    }
}
