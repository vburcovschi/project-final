package com.javarush.jira.profile.web;


import com.javarush.jira.login.internal.UserRepository;
import com.javarush.jira.profile.internal.ProfileMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.login.internal.web.UserTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProfileRestControllerTest{

    public static final String REST_URL = "/api/profile";

    public final static String PROFILE_TEST_JSON =
            "{" +
                    "\"id\":  \"2\","+
                    "\"lastLogin\": \"null\","+
                    "\"mailNotifications\": [" +
                        "\"three_days_before_deadline\"," +
                        "\"one_day_before_deadline\"," +
                        "\"two_days_before_deadline\""+
                        "],"+
                    "\"contacts\": ["+
                        "{"+
                            "\"code\": \"tg\","+
                            "\"value\": \"adminTg\""+
                        "},"+
                        "{"+
                            "\"code\": \"skype\","+
                            "\"value\": \"userSkype\""+
                        "}"+
                    "]}";
    public final static String PROFILE_TEST_JSON_BAD =
            "{" +
                    "\"id\":  \"2\","+
                    "\"lastLogin\": \"null\","+
                    "\"mailNotifications\": [" +
                    "\"three_days_before_deadline\"," +
                    "\"one_day_before_deadline\"," +
                    "\"two_days_before_deadline\""+
                    "],"+
                    "\"contacts\": ["+
                    ""+
                    "\"code\": \"tg\","+
                    "\"value\": \"adminTg\""+
                    "},"+
                    "{"+
                    "\"code\": \"skype\","+
                    "\"value\": \"userSkype\""+
                    "}"+
                    "]}";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    @Autowired
    ProfileMapper mapper;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/profile"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getUnauthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateSuccessfully() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(PROFILE_TEST_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateEmptyContent() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateBadContent() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PROFILE_TEST_JSON_BAD))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }
}