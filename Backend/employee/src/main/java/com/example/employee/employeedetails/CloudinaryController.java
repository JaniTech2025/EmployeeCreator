package com.example.employee.employeedetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cloudinary")
public class CloudinaryController {

    private final Cloudinary cloudinary;
    private final CloudinaryConfig config;

    @Autowired
    public CloudinaryController(CloudinaryConfig config) {
        this.config = config;
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", config.getCloudName(),
                "api_key", config.getApiKey(),
                "api_secret", config.getApiSecret()));
    }

    @GetMapping("/images")
    public List<String> getImages() throws Exception {
        Map result = cloudinary.search()
                .expression("folder:" + config.getFolder())
                .maxResults(30)
                .execute();

        List<Map<String, Object>> resources = (List<Map<String, Object>>) result.get("resources");
        return resources.stream()
                .map(r -> (String) r.get("secure_url"))
                .collect(Collectors.toList());
    }
}