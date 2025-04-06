package com.healthaiharbor.ai.userservice.serviceimpl;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.healthaiharbor.ai.userservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Service implementation for managing a Bloom Filter to optimize user existence checks.
 */
@Service
public class BloomFilterServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(BloomFilterServiceImpl.class);

    private BloomFilter<String> userBloomFilter;
    private final UserRepository userRepository;

    public BloomFilterServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Initializes the Bloom filter with existing usernames from the database.
     * The filter helps in quickly determining if a username is likely to exist.
     */
    @PostConstruct
    public void initializeBloomFilter() {
        try {
            long estimatedUsers = Math.max(userRepository.count(), 1000); // Ensuring a reasonable default size
            userBloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), estimatedUsers, 0.01);

            logger.info("Initializing Bloom filter with an estimated {} users.", estimatedUsers);

            // Preload existing users into the Bloom filter
            List<String> usernames = userRepository.findAllUsernames();
            usernames.forEach(userBloomFilter::put);

            logger.info("Bloom filter successfully populated with {} usernames.", usernames.size());
        } catch (Exception e) {
            logger.error("Error initializing Bloom filter: {}", e.getMessage(), e);
        }
    }

    /**
     * Checks if the given username might be in the system.
     *
     * @param username The username to check.
     * @return True if the username might exist, false otherwise.
     */
    public boolean mightContain(String username) {
        boolean result = userBloomFilter.mightContain(username);
        logger.debug("Bloom filter check for '{}': {}", username, result);
        return result;
    }

    /**
     * Adds a new username to the Bloom filter.
     *
     * @param username The username to add.
     */
    public void addUserToFilter(String username) {
        userBloomFilter.put(username);
        logger.debug("Added '{}' to Bloom filter.", username);
    }
}
