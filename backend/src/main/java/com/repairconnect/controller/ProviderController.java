package com.repairconnect.controller;

import com.repairconnect.entity.*;
import com.repairconnect.repository.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {
    private final ProviderRepository providers; private final ServiceRepository services;
    public ProviderController(ProviderRepository p, ServiceRepository s) { providers=p; services=s; }
    @GetMapping List<Map<String,Object>> search(@RequestParam(required=false, defaultValue="") String city) {
        return providers.findByVerificationStatusAndCityContainingIgnoreCase(VerificationStatus.APPROVED, city).stream().map(this::view).toList();
    }
    @GetMapping("/{id}") Map<String,Object> one(@PathVariable Long id) { var p=providers.findById(id).orElseThrow(); var v=view(p); v.put("services", services.findByProviderId(id)); return v; }
    private Map<String,Object> view(ProviderProfile p) {
        var view=new LinkedHashMap<String,Object>(); view.put("id",p.getId()); view.put("name",p.getUser().getFullName()); view.put("email",p.getUser().getEmail()); view.put("phone",p.getUser().getPhone()); view.put("city",p.getCity()); view.put("address",p.getAddress()); view.put("experience",Objects.requireNonNullElse(p.getExperience(),0)); view.put("description",Objects.requireNonNullElse(p.getDescription(),"")); view.put("hourlyRate",Objects.requireNonNullElse(p.getHourlyRate(),0)); view.put("rating",p.getAverageRating()); view.put("reviews",p.getTotalReviews()); view.put("status",p.getVerificationStatus()); return view;
    }
}
