package com.healthaiharbor.ai.userservice.serviceimpl;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.healthaiharbor.ai.userservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class BloomFilterServiceImpl {

    private BloomFilter<String> userBloomFilter;
    private final UserRepository userRepository;

    public BloomFilterServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initializeBloomFilter() {
        long estimatedUsers = userRepository.count(); // Estimate user count
        userBloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), estimatedUsers, 0.01);

        // Preload existing users into the Bloom filter
        List<String> usernames = userRepository.findAllUsernames();
        usernames.forEach(userBloomFilter::put);
    }

    public boolean mightContain(String username) {
        return userBloomFilter.mightContain(username);
    }

    public void addUserToFilter(String username) {
        userBloomFilter.put(username);
    }
}
