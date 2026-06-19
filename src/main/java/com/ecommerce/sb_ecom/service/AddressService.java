package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.payload.AddressDTO;

import java.util.List;

public interface AddressService {

    AddressDTO createAddress(AddressDTO addressDTO);

    List<AddressDTO> getAllAddress();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getAddressesForUser();

    AddressDTO updateAddressById(AddressDTO addressDTO, Long addressId);

    void deleteAddressById(Long addressId);
}
