package com.wanted.socialMediaIntegratedFeed.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class PostLikeShareTest {
    @Autowired
    private MockMvc mockMvc;

    @Nested
    class like {
        @Test
        @DisplayName("like_Success")
        void likeSuccess() throws Exception {
            mockMvc.perform(patch("/api/v1/content/{id}/like", "1"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("like_NotFound")
        void likeNotFound() throws Exception {
            mockMvc.perform(patch("/api/v1/content/{id}/like", "100"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class share {
        @Test
        @DisplayName("share_Success")
        void shareSuccess() throws Exception {
            mockMvc.perform(patch("/api/v1/content/{id}/share", "1"))
                    .andExpect(status().isOk());
        }
        @Test
        @DisplayName("share_NotFound")
        void shareNotFound() throws Exception {
            mockMvc.perform(patch("/api/v1/content/{id}/share", "100"))
                    .andExpect(status().isNotFound());
        }
    }
}
