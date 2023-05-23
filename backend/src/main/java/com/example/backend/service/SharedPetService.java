package com.example.backend.service;

import com.example.backend.dto.PetRequest;
import com.example.backend.dto.SharedPetRequest;
import com.example.backend.entity.Pet;
import com.example.backend.entity.Schedule;
import com.example.backend.entity.SharedPet;
import com.example.backend.entity.User;
import com.example.backend.repository.PetRepository;
import com.example.backend.repository.SharedPetRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SharedPetService {

    @Autowired
    private final UserService userService;
    @Autowired
    private final PetService petService;
    @Autowired
    private final PetRepository petRepository;
    @Autowired
    private final SharedPetRepository sharedPetRepository;

    public SharedPet sharePet(SharedPetRequest sharedPetRequest, String email, String petName) {

        // 현재 inviter의 pet리스트를 뽑음
        List<Pet> petList = petRepository.findByInviter(email);

        for (Pet pet : petList) {
            // 펫 리스트에서 찾은 petName이 요청한 petName과 같으면
            if (pet.getPetName().equals(petName)) {

                SharedPet sharedPet = SharedPet.builder()
                        .sharedPetId(sharedPetRequest.getSharedPetId())
                        .owner(sharedPetRequest.getOwner())
                        .petName(sharedPetRequest.getPetName())
                        .date(sharedPetRequest.getDate())
                        .hm(sharedPetRequest.getHm())
                        .memo(sharedPetRequest.getMemo())
                        .likes(sharedPetRequest.getLikes())
                        .build();

                return sharedPetRepository.save(sharedPet);
            }
        }

        throw new Error("펫 이름이 없습니다");
    }

    public List<SharedPet> findAllByOwner(String email) {
        List<SharedPet> sharedPets = sharedPetRepository.findAll();

        List<SharedPet> filteredPets = sharedPets.stream()
                .filter(pet -> pet.getOwner().equals(email))
                .collect(Collectors.toList());

        return filteredPets;
    }

    public List<SharedPet> getNSharedPet(String email, Integer N) {
        List<SharedPet> sharedPets = findAllByOwner(email); // sharedPetRepository에서 모든 SharedPet 객체 가져오기

        // sharedPets를 날짜 기준으로 내림차순 정렬
        sharedPets.sort(Comparator.comparing(SharedPet::getDate, Comparator.reverseOrder()));

        if (sharedPets.size() <= N) {
            // N개 이하의 SharedPet이 있는 경우 모두 반환
            return sharedPets;
        } else {
            // N개만 반환
            return sharedPets.subList(0, N);
        }
    }


}
