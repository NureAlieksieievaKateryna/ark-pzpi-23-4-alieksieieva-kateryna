package org.example.reminder.controllers;

import lombok.RequiredArgsConstructor;
import org.example.reminder.admin.facade.AdminFacade;
import org.example.reminder.business.facade.dto.BusinessResponse;
import org.example.reminder.subscription.facade.dto.ModerateBusinessRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminFacade adminFacade;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("checkAllNotVerifiedBusiness")
    public List<BusinessResponse> getAllNotVerifiedBusiness(){
        return adminFacade.getAllNotVerifiedBusiness();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("moderate-business")
    public void moderateBusiness(@RequestBody ModerateBusinessRequest moderateBusinessrequest){
        adminFacade.moderateBusiness(moderateBusinessrequest);
    }

}
