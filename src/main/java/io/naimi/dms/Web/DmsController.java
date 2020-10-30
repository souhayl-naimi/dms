package io.naimi.dms.Web;

import com.sun.xml.bind.v2.runtime.reflect.Lister;
import io.naimi.dms.DAO.*;
import io.naimi.dms.Entities.*;
import io.naimi.dms.Entities.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class DmsController {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @RequestMapping(value = "/home")
    public String home() {
        return "home";
    }

    @RequestMapping(value = "/loginPage")
    public String loginPage() {
        return "loginPage";
    }

    @RequestMapping(value = "/formVendor")
    public String formVendor(Model model) {
        model.addAttribute(new Vendor());
        return "formVendor";
    }

    @PostMapping(value = "saveVendor")
    public String saveVendor(@Valid Vendor vendor, BindingResult bindingResult, Model model) {
        model.addAttribute("saved", vendor);
        if (bindingResult.hasErrors()) return "formVendor";
        vendor.setDateJoined(LocalDate.now());

        vendorRepository.save(vendor);

        Role role = new Role("ROLE_VENDOR", "role Vendeur");
        roleRepository.save(role);
        List<Role> roles = new ArrayList<Role>();
        roles.add(role);

        User user = new User();
        user.setCin(vendor.getCin());
        user.setPassword(bCryptPasswordEncoder.encode(vendor.getPassword()));
        user.setEnabled(true);
        user.setRoles(roles);

        userRepository.save(user);

        return "home";
    }

    @RequestMapping(value = "/deliveryRequestForm")
    public String deliveryRequestForm(Model model) {

        List<City> villeList = cityRepository.findAll();
        model.addAttribute(new Package());
        model.addAttribute("villeList", villeList);

        return "deliveryRequestForm";
    }

    @PostMapping(value = "saveDeliveryRequest")
    public String saveDeliveryRequest(@Valid Package aPackage, HttpServletRequest request,
                                      BindingResult bindingResult, Model model,
                                      String firstcom) {
        model.addAttribute("saved", aPackage);
        if (bindingResult.hasErrors()) return "deliveryRequestForm";
        aPackage.setDateCreated(LocalDateTime.now());
        aPackage.setVendor(vendorRepository.findByCin(request.getUserPrincipal().getName()));
        Comment comment = new Comment();
        comment.setComment(firstcom);
        comment.setTimeCommented(LocalDateTime.now());
        comment.setAPackage(aPackage);
        List<Comment> comments = new ArrayList<Comment>();
        comments.add(comment);
        aPackage.setComments(comments);
        Status status = new Status();
        status.setStatus("livraison demandé");
        status.setTimeUpdated(LocalDateTime.now());
        status.setApackage(aPackage);
        List<Status> statuses = new ArrayList<>();
        statuses.add(status);
        aPackage.setStatuses(statuses);
        packageRepository.save(aPackage);
        commentRepository.save(comment);
        statusRepository.save(status);
        return "redirect:/deliveryRequests";
    }


    @RequestMapping(value = "/deliveryRequests", method = GET)
    public String cinemas(Model model,
                          @RequestParam(name = "page", defaultValue = "0") int page,
                          @RequestParam(name = "size", defaultValue = "1") int size,
                          HttpServletRequest request) {
        Page<Package> packagePage = packageRepository.findByVendor_Cin(request.getUserPrincipal().getName(), PageRequest.of(page, size));
        model.addAttribute("result", packagePage.getTotalElements());
        model.addAttribute("pages", new int[packagePage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("packagesList", packagePage.getContent());
        model.addAttribute("size", size);
        model.addAttribute("username", request.getUserPrincipal().getName());
        return "deliveryRequests";
    }

    @RequestMapping(value = "/moreInfo")
    private String moreInfo(Long id, Model model) {
        Package aPackage = packageRepository.findById(id).get();
        model.addAttribute("package", aPackage);
        return "moreInfo";
    }

    @PostMapping(value = "addComment")
    public String addComment(@RequestParam(name = "id") Long id, String comment) {
        Package aPackage = packageRepository.findById(id).get();
        Comment comment1 = new Comment();
        comment1.setAPackage(aPackage);
        comment1.setComment(comment);
        comment1.setTimeCommented(LocalDateTime.now());
        aPackage.getComments().add(comment1);
        packageRepository.save(aPackage);
        commentRepository.save(comment1);
        return "redirect:/moreInfo?id=" + id;
    }

    @PostMapping(value = "updateStatus")
    public String updateStatus(@RequestParam(name = "id") Long id, String stat) {
        Package aPackage = packageRepository.findById(id).get();
        Status status = new Status();
        status.setApackage(aPackage);
        status.setStatus(stat);
        status.setTimeUpdated(LocalDateTime.now());
        aPackage.getStatuses().add(status);
        packageRepository.save(aPackage);
        statusRepository.save(status);
        return "redirect:/moreInfo?id=" + id;
    }

    @RequestMapping(value = "/cities", method = GET)
    public String cities(Model model,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "size", defaultValue = "6") int size,
                         @RequestParam(name = "name", defaultValue = "") String name) {
        Page<City> villePage = cityRepository.findByNameContainsIgnoreCase(name, PageRequest.of(page, size));
        model.addAttribute("result", villePage.getTotalElements());
        model.addAttribute("villeList", villePage.getContent());
        model.addAttribute("pages", new int[villePage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("name", name);
        model.addAttribute("size", size);
        return "cities";
    }
    @RequestMapping(value = "/deleteCity", method = RequestMethod.POST)
    public String deleteCity(Long id, int page, int size, String name) {
        cityRepository.deleteById(id);
        return "redirect:/cities?page=" + page + "&size=" + size + "&name=" + name + "";
    }

    @RequestMapping(value = "/formCity", method = RequestMethod.GET)
    public String formPatient(Model model) {
        model.addAttribute("city", new City());
        return "formCity";
    }

    @RequestMapping(value = "editCity", method = RequestMethod.GET)
    public String editPatient(Model model, Long id) {
        City city = cityRepository.findById(id).get();
        System.out.println(id);
        model.addAttribute("city", city);
        return "formCity";
    }

    @PostMapping(value = "saveCity")
    public String savePatient(@Valid City city, BindingResult bindingResult, Model model) {
        model.addAttribute("saved", city);
        if (bindingResult.hasErrors()) return "formCity";
        cityRepository.save(city);
        return "redirect:/cities";

    }

}
