package com.ecommerce.sb_ecom.controller;


import com.ecommerce.sb_ecom.payload.AddressDTO;
import com.ecommerce.sb_ecom.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    private AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO addressDTO1 = addressService.createAddress(addressDTO);
        return new ResponseEntity<>(addressDTO1, HttpStatus.CREATED);
    }


    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddress(){
        List<AddressDTO> addressDTOS = addressService.getAllAddress();
        return new ResponseEntity<>(addressDTOS, HttpStatus.OK);
    }


    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        AddressDTO addressDTO = addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressesForUser(){
        List<AddressDTO> addressDTOS = addressService.getAddressesForUser();
        return new ResponseEntity<>(addressDTOS, HttpStatus.OK);
    }


    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressById(@RequestBody AddressDTO addressDTO, @PathVariable Long addressId){
        AddressDTO addressDTO1 = addressService.updateAddressById(addressDTO, addressId);
        return new ResponseEntity<>(addressDTO1, HttpStatus.OK);
    }


    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddressById(@PathVariable Long addressId){
        addressService.deleteAddressById(addressId);
        return new ResponseEntity<>("Address has been deleted successfully", HttpStatus.OK);
    }

}
