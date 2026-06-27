package com.ecommerce.sb_ecom.service.impl;

import com.ecommerce.sb_ecom.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Address;
import com.ecommerce.sb_ecom.model.AppRole;
import com.ecommerce.sb_ecom.model.User;
import com.ecommerce.sb_ecom.payload.AddressDTO;
import com.ecommerce.sb_ecom.repository.AddressRepository;
import com.ecommerce.sb_ecom.repository.UserRepository;
import com.ecommerce.sb_ecom.service.AddressService;
import com.ecommerce.sb_ecom.utils.AuthUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AuthUtils authUtils;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public AddressServiceImpl(AuthUtils authUtils, AddressRepository addressRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.authUtils = authUtils;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        User user = authUtils.loggedInUser();
        addressDTO.setAddressId(null);
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(user);
        user.getAddresses().add(address);
        address = addressRepository.save(address);
        userRepository.save(user);
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddress() {
        List<Address> addresses = addressRepository.findAll();
        List<AddressDTO> addressDTOs = new ArrayList<>();
        if (!addresses.isEmpty()) {
            addresses.forEach(add -> {
                AddressDTO a = modelMapper.map(add, AddressDTO.class);
                addressDTOs.add(a);
            });
        }
        return addressDTOs;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address not found"));
        User user = authUtils.loggedInUser();
        if (user.getRoles().contains(AppRole.ROLE_ADMIN) || address.getUser().getUserId().equals(user.getUserId())) {
            return modelMapper.map(address, AddressDTO.class);
        }
        throw new AccessDeniedException("Access denied");
    }

    @Override
    public List<AddressDTO> getAddressesForUser() {
        User user = authUtils.loggedInUser();
        List<Address> addresses = user.getAddresses();
        List<AddressDTO> addressDTOs = new ArrayList<>();
        if (!addresses.isEmpty()) {
            addresses.forEach(add -> {
                AddressDTO a = modelMapper.map(add, AddressDTO.class);
                addressDTOs.add(a);
            });
        }
        return addressDTOs;
    }

    @Override
    public AddressDTO updateAddressById(AddressDTO addressDTO, Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address not found"));
        User user = authUtils.loggedInUser();
        if (!user.getRoles().contains(AppRole.ROLE_ADMIN) && !address.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Access denied");
        }
        address.setCity(addressDTO.getCity());
        address.setCountry(addressDTO.getCountry());
        address.setStreet(addressDTO.getStreet());
        address.setState(addressDTO.getState());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setBuildingName(addressDTO.getBuildingName());
        address = addressRepository.save(address);
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public void deleteAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address not found"));
        User user = authUtils.loggedInUser();
        if (!user.getRoles().contains(AppRole.ROLE_ADMIN) && !address.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Access denied");
        }
        addressRepository.delete(address);
    }
}
