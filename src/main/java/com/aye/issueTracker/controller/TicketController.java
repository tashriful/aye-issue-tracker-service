package com.aye.issueTracker.controller;

import com.aye.issueTracker.common.CustomErrorResponse;
import com.aye.issueTracker.exception.InvalidRequestDataException;
import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.model.Attachment;
import com.aye.issueTracker.model.Employee;
import com.aye.issueTracker.service.*;
import com.aye.issuetrackerdto.entityDto.EmployeeDto;
import com.aye.issuetrackerdto.entityDto.TeamDto;
import com.aye.issuetrackerdto.entityDto.TicketDto;
import com.aye.issuetrackerdto.entityDto.TicketHistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService  ticketService;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TicketHistoryService ticketHistoryService;

//    @PostMapping("/")
//    public ResponseEntity<?> saveTicket(@RequestBody TicketDto ticketDto){
//        try {
//            String attachmentId = attachmentService.addFile(ticketDto.getFile());
//            TicketDto createdDto = ticketService.saveTicket(ticketDto);
////            createdDto.setFile(attachmentId);
//            return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
//        }
//        catch (Exception e){
//            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping("/submit")
    public ResponseEntity<?> saveTicket(@RequestParam("summary") String summary,
                                        @RequestParam("description") String description,
                                        @RequestParam("departmentId") Long departmentId,
                                        @RequestParam(value = "teamId", required = false) Long teamId,
                                        @RequestParam("ticketType") String ticketType,
                                        @RequestParam("priority") String priority,
                                        @RequestParam("status") String status,
                                        @RequestParam("assignedToUser") Long assignedToUser,
                                        @RequestParam("targetResolutionDate") String targetResolutionDate,
                                        @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        System.out.println("start...");

        TicketDto ticketDto = new TicketDto();

        LocalDate formattedtargetResolutionDate = LocalDate.parse(targetResolutionDate);
        System.out.println("Formatted date: " + formattedtargetResolutionDate);

        ticketDto.setSummary(summary);
        ticketDto.setDescription(description);
        ticketDto.setDepartmentId(departmentId);
        if(teamId != null) {
            ticketDto.setTeamId(teamId);
        }
        else {
            ticketDto.setTeamId(null);
        }
        ticketDto.setTicketType(ticketType);
        ticketDto.setPriority(priority);
        ticketDto.setAssignedToUser(assignedToUser);
        ticketDto.setStatus(status);
        ticketDto.setTargetResolutionDate(formattedtargetResolutionDate);
        if(!file.isEmpty()) {
            ticketDto.setFile(file);
        }
        else {
            ticketDto.setFile(null);
        }

        try {
            if(ticketDto.getFile() != null){
            String attachmentId = attachmentService.addFile(ticketDto.getFile());
            ticketDto.setFileId(attachmentId);
            }
            else {
                ticketDto.setFileId(null);
            }
            String createdByUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long createdByUserId = userService.getUserIdByUserName(createdByUser).getId();

            EmployeeDto employeeDto = employeeService.getEmployeeByUser(createdByUserId);

            ticketDto.setCreatedById(employeeDto.getId());
            ticketDto.setAssignedById(employeeDto.getId());


            TicketDto createdDto = ticketService.saveTicket(ticketDto);
            createdDto.setFile(createdDto.getFile());
            TicketHistoryDto createdTicketHistory = ticketHistoryService.saveTicketHistory(createdDto);
            return new ResponseEntity<>(createdDto, HttpStatus.OK);
        }
        catch (ResourceNotFoundException | IOException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.ACCEPTED);
        }
        catch (InvalidRequestDataException e){
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/")
    public List<TicketDto> getAllTickets() {
        return ticketService.getAllTicket();
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateTicketStatus(@PathVariable("id") Long id, @RequestBody TicketDto ticketDto){
        try {
            TicketDto ticketDto1 = ticketService.updateTicketStatus(id, ticketDto);
            return ResponseEntity.ok(ticketDto1);
        }
        catch (ResourceNotFoundException e){
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now()), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PutMapping("/assignedTo/{id}")
//    public ResponseEntity<?> updateAssignedTo(@PathVariable("id") Long id, @RequestBody TicketDto ticketDto){
//        try {
//            TicketDto ticketDto1 = ticketService.updateAssignedTo(id, ticketDto);
//            return ResponseEntity.ok(ticketDto1);
//        }
//        catch (ResourceNotFoundException e){
//            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now()), HttpStatus.NOT_FOUND);
//        } catch (Exception e){
//            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable("id") Long id) throws ResourceNotFoundException, IOException {
        try {
            TicketDto ticketDto = ticketService.getTicketById(id);
            return ResponseEntity.ok().body(ticketDto);
        }
        catch (ResourceNotFoundException e){
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now()), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable("id") Long id) throws ResourceNotFoundException {
        try {
            ticketService.deleteTicket(id);
        }
        catch (ResourceNotFoundException e){
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now()), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<> (HttpStatus.NO_CONTENT);
    }

    @GetMapping("/department")
    public ResponseEntity<?> getTicketByDepartment() throws ResourceNotFoundException, IOException {
        String createdByUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long createdByUserId = userService.getUserIdByUserName(createdByUser).getId();
        List<TicketDto> ticketDtos = ticketService.getTicketByDepartment(createdByUserId);
        return ResponseEntity.ok().body(ticketDtos);
    }

    @GetMapping("/assignedTo")
    public ResponseEntity<?> getTicketByAssignedTo() throws ResourceNotFoundException, IOException {
        String createdByUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long createdByUserId = userService.getUserIdByUserName(createdByUser).getId();
        List<TicketDto> ticketDtos = ticketService.getTicketByAssignedTo(createdByUserId);
        return ResponseEntity.ok().body(ticketDtos);
    }

    @GetMapping("/createdBy")
    public ResponseEntity<?> getTicketsByCreatedBy() throws ResourceNotFoundException, IOException {
        try {
            String createdByUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long createdByUserId = userService.getUserIdByUserName(createdByUser).getId();
            List<TicketDto> ticketDtos = ticketService.getTicketsByCreatedBy(createdByUserId);
            if(ticketDtos != null) {
                return ResponseEntity.ok().body(ticketDtos);
            }
            else{
                return ResponseEntity.ok(null);
            }
        }
        catch (ResourceNotFoundException e){
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now()), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/team/{id}")
    public ResponseEntity<?> getTicketByTeam(@PathVariable("id") Long id) throws ResourceNotFoundException, IOException {
        List<TicketDto> ticketDtos = ticketService.getTicketByTeam(id);
        return ResponseEntity.ok().body(ticketDtos);
    }

    @PutMapping("/updateTicket/{id}")
    public ResponseEntity<?> updateTicket(@PathVariable("id") Long ticketId, @RequestBody TicketDto ticketDto){
        try {
            TicketDto updatedTicketDto = ticketService.updateTicket(ticketId, ticketDto);
            return ResponseEntity.ok().body(updatedTicketDto);
        } catch (InvalidRequestDataException | ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal Server Error :- " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }














    @Autowired
    private MongoTemplate mongoTemplate;


    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Attachment image = new Attachment();
            image.setFileName(file.getOriginalFilename());
            image.setContentType(file.getContentType());
            image.setSize(file.getSize());
            image.setContent(file.getBytes());

            mongoTemplate.insert(image);

            return ResponseEntity.ok("Image uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Autowired
    private ImageService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(attachmentService.addFile(file), HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String id) throws IOException {
        Attachment loadFile = attachmentService.downloadFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(loadFile.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + loadFile.getFileName() + "\"")
                .body(new ByteArrayResource(loadFile.getContent()));
    }




}
