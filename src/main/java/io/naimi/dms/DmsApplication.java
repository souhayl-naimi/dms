package io.naimi.dms;

import com.sun.xml.bind.v2.runtime.reflect.Lister;
import io.naimi.dms.DAO.PackageRepository;
import io.naimi.dms.DAO.RoleRepository;
import io.naimi.dms.DAO.UserRepository;
import io.naimi.dms.DAO.VendorRepository;
import io.naimi.dms.Entities.*;
import io.naimi.dms.Entities.Package;
import io.naimi.dms.Services.IDmsInitializer;
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
    private IDmsInitializer iDmsInitializer;

    @Override
    public void run(String... args) throws Exception {
        iDmsInitializer.initCities();
        iDmsInitializer.initVendors();
        iDmsInitializer.initDms();
        iDmsInitializer.initPackages();
        iDmsInitializer.initUsers();

    }
}
