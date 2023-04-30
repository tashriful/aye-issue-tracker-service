package com.aye.issueTracker;

import com.aye.issuetrackerdto.entityDto.EmployeeDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class IssueTrackerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testCreateEmployee() throws Exception {
		// Create a new employee DTO object
		EmployeeDto employeeDto= new EmployeeDto(null, "Naimul", "MANAGER", "Dhaka", 1L, "Naimul",1L, "IT", true, 1L, "Development",true);

		// Convert the DTO object to JSON
		String employeeDtoJson = objectMapper.writeValueAsString(employeeDto);

		// Send a POST request to the create employee endpoint with the JSON payload
		mockMvc.perform(MockMvcRequestBuilders.post("/employees")
						.contentType(MediaType.APPLICATION_JSON)
						.content(employeeDtoJson))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}


}
