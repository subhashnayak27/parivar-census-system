package com.parivar.census.state.controller.StateController;

import com.parivar.census.state.entity.State.State;
import com.parivar.census.state.service.StateService.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/states")
@RequiredArgsConstructor
public class StateController {

    private final StateService service;

    @PostMapping
    public State save(@RequestBody State state) {
        return service.save(state);
    }
}