package io.naimi.dms.Web;

import io.naimi.dms.DAO.*;
import io.naimi.dms.Entities.*;
import io.naimi.dms.Entities.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LivreurController {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private DeliveryManRepository deliveryManRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "/formDeliveryGuy")
    public String formDeliveryGuy(Model model) {
        List<City> villeList = cityRepository.findAll();
        model.addAttribute("villeList", villeList);
        model.addAttribute("livreur", new DeliveryMan());
        return "formDeliveryGuy";
    }

    @PostMapping(value = "saveLivreur")
    public String saveLivreur(DeliveryMan deliveryMan, Model model) {

        model.addAttribute("livreur", deliveryMan);

        deliveryMan.setDateJoined(LocalDate.now());
        deliveryManRepository.save(deliveryMan);

        Role role = new Role("ROLE_LIVREUR", "role livreur");
        roleRepository.save(role);
        List<Role> roles = new ArrayList<Role>();
        roles.add(role);

        User user = new User();
        user.setCin(deliveryMan.getCin());
        user.setPassword(bCryptPasswordEncoder.encode(deliveryMan.getPassword()));
        user.setEnabled(true);
        user.setRoles(roles);
        user.setUsername("G" + deliveryMan.getId());
        userRepository.save(user);

        return "redirect:/welcomePage?username=" + user.getUsername() + "&name=" + deliveryMan.getFullName();
    }

    @RequestMapping(value = "toBeDelivered")
    public String toBeDelivered(Model model,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "2") int size,
                                @RequestParam(name = "name", defaultValue = "") String name,
                                @RequestParam(name = "cityID", defaultValue = "0") Long cityID,
                                HttpServletRequest request) {

        Long vendorID = Long.parseLong(request.getUserPrincipal().getName().substring(1));


        Page<Package> packagePage = packageRepository.findByDeliveryMan_Id(vendorID, PageRequest.of(page, size));

        model.addAttribute("pages", new int[packagePage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("name", name);
        model.addAttribute("packagesList", packagePage.getContent());
        model.addAttribute("result", packagePage.getTotalElements());
        model.addAttribute("username", request.getUserPrincipal().getName());

        return "toBeDelivered";
    }

    @RequestMapping(value = "deliveryMen")
    public String deliveryMen(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "size", defaultValue = "10") int size, @RequestParam(name = "cityID", defaultValue = "0") Long cityID,
                              @RequestParam(name = "cityID", defaultValue = "0") Long id) {
        Page<DeliveryMan> deliveryMen = null;
        if (cityID != 0) {
            deliveryMen = deliveryManRepository.findByCity_Id(id, PageRequest.of(page, size));
        } else if (cityID == 0) {
            deliveryMen = deliveryManRepository.findAll(PageRequest.of(page, size));
        }
        List<City> villeList = cityRepository.findAll();
        model.addAttribute("cityID", cityID);
        model.addAttribute("villeList", villeList);
        model.addAttribute("pages", new int[deliveryMen.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("deliveryMen", deliveryMen.getContent());
        model.addAttribute("result", deliveryMen.getTotalElements());

        return "deliveryMen";
    }

    @RequestMapping(value = "/deleteLivreur", method = RequestMethod.POST)
    public String deleteLivreur(Long id, int page, int size, int cityID) {
        deliveryManRepository.deleteById(id);
        return "redirect:/deliveryMen?page=" + page + "&size=" + size + "" + "&cityID=" + cityID;
    }


    @RequestMapping(value = "/profileLivreur")
    private String profile(HttpServletRequest request, Model model) {
        Long deliveryManId = Long.parseLong(request.getUserPrincipal().getName().substring(1));
        DeliveryMan deliveryMan = deliveryManRepository.findById(deliveryManId).get();
        model.addAttribute("deliveryMan", deliveryMan);
        return "profileLivreur";
    }

    @PostMapping(value = "setDeliveryMan")
    public String setDeliveryMan(@RequestParam(name = "id") Long id, Long deliveryMan,Model model) {
        Package aPackage = packageRepository.findById(id).get();
        DeliveryMan deliveryMan1 = deliveryManRepository.findById(deliveryMan).get();
        aPackage.setDeliveryMan(deliveryMan1);
        packageRepository.save(aPackage);
        return "redirect:/moreInfo?id=" + id + "&dID="+deliveryMan1.getId();
    }


}
