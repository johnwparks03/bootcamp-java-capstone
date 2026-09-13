package assembly.general.api.controllers;

import assembly.general.api.dto.ProfileDto;
import assembly.general.api.dto.ProfileResponse;
import assembly.general.api.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(Authentication authentication){
        UUID userId = UUID.fromString(authentication.getName());

        ProfileResponse response = profileService.getProfile(userId);

        if(response.isSuccess()){
            return ResponseEntity.ok().body(response);
        } else{
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
