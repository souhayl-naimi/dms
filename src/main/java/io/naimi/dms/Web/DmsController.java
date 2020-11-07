package io.naimi.dms.Web;

import io.naimi.dms.DAO.*;
import io.naimi.dms.Entities.*;
import io.naimi.dms.Entities.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private StatusRepository statusRepository;
    @Autowired
    private PackageRepository packageRepository;
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

    @RequestMapping(value = "/welcomePage")
    public String welcomePage() {
        return "welcomePage";
    }

    @RequestMapping(value = "/loginPage")
    public String loginPage() {

        vendorRepository.findAll().forEach(vendor -> {
            vendor.setUsername("G" + vendor.getId());
            vendorRepository.save(vendor);
        });
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
        user.setUsername("G" + vendor.getId());
        userRepository.save(user);

        return "redirect:/welcomePage?username=" + user.getUsername() + "&name=" + vendor.getFullName();
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
        Long vendorID = Long.parseLong(request.getUserPrincipal().getName().substring(1));
        System.out.println(vendorID);
        aPackage.setVendor(vendorRepository.findById(vendorID).get());

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
    public String deliveryRequests(Model model,
                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "2") int size,
                                   @RequestParam(name = "name", defaultValue = "") String name,
                                   @RequestParam(name = "cityID", defaultValue = "0") Long cityID,
                                   HttpServletRequest request) {

        Page<Package> packagePage = null;

        Long vendorID = Long.parseLong(request.getUserPrincipal().getName().substring(1));
        if (cityID != 0) {
            packagePage = packageRepository.findByVendor_IdAndNotDeletableAndReferenceContainsAndCity_Id(vendorID, false, name, cityID, PageRequest.of(page, size));
        }
        else if(cityID == 0) {
            packagePage = packageRepository.findByVendor_IdAndNotDeletableAndReferenceContains(vendorID, false, name, PageRequest.of(page, size));
        }
        List<City> villeList = cityRepository.findAll();
        model.addAttribute("villeList", villeList);
        model.addAttribute("pages", new int[packagePage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("name", name);
        model.addAttribute("cityID", cityID);
        model.addAttribute("packagesList", packagePage.getContent());
        model.addAttribute("result", packagePage.getTotalElements());
        model.addAttribute("username", request.getUserPrincipal().getName());
        return "deliveryRequests";
    }

    @RequestMapping(value = "/deliveryRequestsByUsername", method = GET)
    public String deliveryRequestsByUsername(Model model,
                                             @RequestParam(name = "page", defaultValue = "0") int page,
                                             @RequestParam(name = "size", defaultValue = "2") int size,
                                             @RequestParam(name = "name", defaultValue = "") String name,
                                             Long id) {
        Page<Package> packagePage = packageRepository.findByVendor_IdAndReferenceContains(id, name, PageRequest.of(page, size));
        model.addAttribute("pages", new int[packagePage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("name", name);
        model.addAttribute("id", id);
        model.addAttribute("packagesList", packagePage.getContent());
        model.addAttribute("result", packagePage.getTotalElements());
        return "deliveryRequestsByUsername";
    }

    @RequestMapping(value = "/deliveries", method = GET)
    public String deliveries(Model model,
                             @RequestParam(name = "page", defaultValue = "0") int page,
                             @RequestParam(name = "size", defaultValue = "2") int size,
                             @RequestParam(name = "name", defaultValue = "") String name,
                             HttpServletRequest request) {

        Long vendorID = Long.parseLong(request.getUserPrincipal().getName().substring(1));
        Page<Package> packagePage = packageRepository.findByVendor_IdAndNotDeletableAndReferenceContains(vendorID, true, name, PageRequest.of(page, size));
//        Page<Package> packagePage = packageRepository.findByVendor_IdAndReferenceContains(vendorID,name, PageRequest.of(page, size));
        model.addAttribute("pages", new int[packagePage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("name", name);
        model.addAttribute("packagesList", packagePage.getContent());
        model.addAttribute("result", packagePage.getTotalElements());
        return "deliveries";
    }

    @RequestMapping(value = "/vendors", method = GET)
    public String vendors(Model model,
                          @RequestParam(name = "page", defaultValue = "0") int page,
                          @RequestParam(name = "size", defaultValue = "2") int size,
                          @RequestParam(name = "code", defaultValue = "") String code) {

        Page<Vendor> vendorPage = vendorRepository.findByUsernameContains(code, PageRequest.of(page, size));
        model.addAttribute("pages", new int[vendorPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("code", code);
        model.addAttribute("vendorList", vendorPage.getContent());
        model.addAttribute("result", vendorPage.getTotalElements());
        return "vendors";
    }

    @RequestMapping(value = "editPackage", method = RequestMethod.GET)
    public String editPackage(Model model, Long id) {
        Package aPackage = packageRepository.findById(id).get();
        List<City> villeList = cityRepository.findAll();
        model.addAttribute("villeList", villeList);
        model.addAttribute("package", aPackage);
        return "deliveryRequestFormUpdate";
    }

    @PostMapping(value = "saveDeliveryRequestUpdate")
    public String saveDeliveryRequestUpdate(@Valid Package aPackage, HttpServletRequest request, Model model) {
        Long vendorID = Long.parseLong(request.getUserPrincipal().getName().substring(1));
        aPackage.setVendor(vendorRepository.findById(vendorID).get());
        packageRepository.save(aPackage);
        return "redirect:/deliveryRequests";

    }

    @RequestMapping(value = "/deleteRequest", method = RequestMethod.POST)
    public String deleteCinema(Long id, int page, int size) {
        packageRepository.deleteById(id);
        return "redirect:/deliveryRequests?page=" + page + "&size=" + size + "";
    }

    @RequestMapping(value = "/moreInfo")
    private String moreInfo(Long id, Model model) {
        Package aPackage = packageRepository.findById(id).get();
        model.addAttribute("package", aPackage);
        return "moreInfo";
    }

    @RequestMapping(value = "/profile")
    private String profile(HttpServletRequest request, Model model) {
        Long vendorID = Long.parseLong(request.getUserPrincipal().getName().substring(1));
        Vendor vendor = vendorRepository.findById(vendorID).get();
        model.addAttribute("vendor", vendor);
        return "profile";
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
        aPackage.setNotDeletable(true);
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
