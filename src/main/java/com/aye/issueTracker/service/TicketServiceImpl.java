package com.aye.issueTracker.service;

import com.aye.issueTracker.exception.InvalidRequestDataException;
import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.model.*;
import com.aye.issueTracker.repository.TicketRepository;
import com.aye.issuetrackerdto.entityDto.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService{

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentHeadService departmentHeadService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamHeadService teamHeadService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TicketHistoryService ticketHistoryService;




    @Override
    public TicketDto saveTicket(TicketDto ticketDto) {

        validateRequest(ticketDto);


        EmployeeDto assignedToUser = employeeService.getEmployeeById(ticketDto.getAssignedToUser());
        EmployeeDto assignedByUser = employeeService.getEmployeeById(ticketDto.getAssignedById());
        EmployeeDto createdBy = employeeService.getEmployeeById(ticketDto.getCreatedById());
        DepartmentDto departmentDto = departmentService.getDepartmentById(ticketDto.getDepartmentId());
        EmployeeDto departmentHeadDto = employeeService.getDepartmentHead(ticketDto.getDepartmentId(), true);

        TeamDto teamDto = null;
        EmployeeDto teamHeadDto = null;

        if (ticketDto.getTeamId() != null) {
            teamDto = teamService.getTeamById(ticketDto.getTeamId()).get();
            teamHeadDto = employeeService.getTeamHead(ticketDto.getTeamId(), true);
        }


        Ticket ticket = new Ticket();


        ticket.setId(sequenceGeneratorService.generateSequence(Ticket.SEQUENCE_NAME));
        ticket.setSummary(ticketDto.getSummary());
        ticket.setDescription(ticketDto.getDescription());
        ticket.setFileId(ticketDto.getFileId());
        ticket.setCreatedBy(this.modelMapper.map(createdBy, Employee.class));
        ticket.setCreatedDateTime(ticketDto.getCreatedDateTime());
        ticket.setDepartment(this.modelMapper.map(departmentDto, Department.class));
        ticket.setDepartmentHead(modelMapper.map(departmentHeadDto, Employee.class));

        if (teamDto != null) {
            ticket.setTeam(this.modelMapper.map(teamDto, Team.class));
            ticket.setTeamHead(this.modelMapper.map(teamHeadDto, Employee.class));
        }else{
            ticket.setTeam(null);
            ticket.setTeamHead(null);
        }

        ticket.setTicketType(ticketDto.getTicketType());
        ticket.setAssignedTo(this.modelMapper.map(assignedToUser, Employee.class));
        ticket.setAssignedBy(this.modelMapper.map(assignedByUser, Employee.class));
        ticket.setPrirority(ticketDto.getPriority());
        ticket.setStatus(ticketDto.getStatus());
        ticket.setTicketType(ticketDto.getTicketType());
        ticket.setTargetResolutionDate(ticketDto.getTargetResolutionDate());
        ticket.setActualResolutionDate(ticketDto.getActualResolutionDate());
        ticket.setResolutionSummary(ticketDto.getResolutionSummary());

        Ticket createdTicket = ticketRepository.save(ticket);
        return convertToDto(createdTicket);
    }

    private void validateRequest(TicketDto ticketDto) {
        if (ticketDto == null) {
            throw new InvalidRequestDataException("Invalid Request Object");
        }

        if (ticketDto.getSummary() == null || ticketDto.getSummary().isEmpty()) {
            throw new InvalidRequestDataException("Summary Can't be empty");
        }

        if (ticketDto.getDepartmentId() == null) {
            throw new InvalidRequestDataException("Department Can't be empty");
        }
        if (ticketDto.getAssignedById() == null) {
            throw new InvalidRequestDataException("AssignedById Can't be empty");
        }
        if (ticketDto.getAssignedToUser() == null) {
            throw new InvalidRequestDataException("AssignedToUser Can't be empty");
        }
        if (ticketDto.getPriority() == null || ticketDto.getPriority().isEmpty()){
            throw new InvalidRequestDataException("Prirority Can't be empty");
        }
        if (ticketDto.getStatus() == null || ticketDto.getStatus().isEmpty()){
            throw new InvalidRequestDataException("Status Can't be empty");
        }




    }

    @Override
    public List<TicketDto> getAllTicket() {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream().map(ticket -> {
            Attachment file = null;
            try {
                file = attachmentService.downloadFile(ticket.getFileId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return convertToDtoWithAttachment(ticket, file);
        }).collect(Collectors.toList());
    }

    @Override
    public TicketDto getTicketById(Long id) throws IOException {
        Optional<Ticket> optionalticket = ticketRepository.findById(id);
        if(optionalticket.isPresent()) {
            Ticket ticket = optionalticket.get();
            Attachment file = attachmentService.downloadFile(ticket.getFileId());

            TicketDto ticketDto = new TicketDto();
            ticketDto.setId(ticket.getId());
            ticketDto.setSummary(ticket.getSummary());
            ticketDto.setDescription(ticket.getDescription());
            ticketDto.setFile(null); // Set to null if not needed
            ticketDto.setFileId(ticket.getFileId());
            ticketDto.setFilename(file.getFileName());
            ticketDto.setContentType(file.getContentType());
            ticketDto.setSize(file.getSize());
//        ticketDto.setContent(file.getContent());
            ticketDto.setCreatedById(ticket.getCreatedBy().getId());
            ticketDto.setCreatedDateTime(ticket.getCreatedDateTime());
            ticketDto.setDepartmentId(ticket.getDepartment().getId());
            ticketDto.setDepartmentName(ticket.getDepartment().getName());

            if (ticket.getTeam() != null && ticket.getTeamHead() != null) {
                ticketDto.setTeamId(ticket.getTeam().getId());
                ticketDto.setTeamName(ticket.getTeam().getName());
                ticketDto.setTeamHeadName(ticket.getTeamHead().getUser().getName());
                ticketDto.setTeamHeadId(ticket.getTeamHead().getUser().getId());
            } else {
                ticketDto.setTeamId(null);
                ticketDto.setTeamName(null);
                ticketDto.setTeamHeadName(null);
                ticketDto.setTeamHeadId(null);
            }

            ticketDto.setDepartmentHeadName(ticket.getDepartmentHead().getUser().getName());
            ticketDto.setDepartmentHeadId(ticket.getDepartmentHead().getUser().getId());
            ticketDto.setTicketType(ticket.getTicketType());
            ticketDto.setAssignedToUser(ticket.getAssignedTo().getId());
            ticketDto.setPriority(ticket.getPrirority());
            ticketDto.setStatus(ticket.getStatus());
            ticketDto.setTargetResolutionDate(ticket.getTargetResolutionDate());
            ticketDto.setActualResolutionDate(ticket.getActualResolutionDate());
            ticketDto.setResolutionSummary(ticket.getResolutionSummary());


            return ticketDto;
        }
        else {
            throw new ResourceNotFoundException("Ticket not found with id: " + id);
        }

    }

    @Override
    public List<TicketDto> getTicketByDepartment(Long id) {
        List<Ticket> tickets = ticketRepository.findByDepartment(id);
        return tickets.stream().map(ticket -> {
            Attachment file = null;
            try {
                file = attachmentService.downloadFile(ticket.getFileId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return convertToDtoWithAttachment(ticket, file);
        }).collect(Collectors.toList());
    }

    @Override
    public List<TicketDto> getTicketByAssignedTo(Long id) {
        List<Ticket> tickets = ticketRepository.findByAssignedTo(id);

        return tickets.stream().map(ticket -> {
            Attachment file = null;
            try {
                file = attachmentService.downloadFile(ticket.getFileId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return this.convertToDtoWithAttachment(ticket, file);
        }).collect(Collectors.toList());

    }

    @Override
    public List<TicketDto> getTicketByTeam(Long id) throws IOException {
        List<Ticket> tickets = ticketRepository.findByTeam(id);
        return tickets.stream()
                .map(t -> {
                    Attachment file = null;
                    try {
                        file = attachmentService.downloadFile(t.getFileId());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return this.convertToDtoWithAttachment(t, file);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTicket(Long id) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isPresent()) {
            ticketRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Ticket not found for this id: " + id);
        }
    }

    @Override
    public TicketDto updateTicketStatus(Long id, TicketDto ticketDto) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if(ticket.isPresent()){
            Ticket ticket1 = ticket.get();
            ticket1.setStatus(ticketDto.getStatus());
            Ticket updatedTicket = ticketRepository.save(ticket1);
            return convertToDto(updatedTicket);
        }
        else {
            throw new ResourceNotFoundException("Ticket Not found with this id"+id);
        }

    }

    @Override
    public TicketDto updateAssignedTo(Long id, TicketDto ticketDto) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if(ticket.isPresent()){
            Ticket prevTicket = ticket.get();

            Employee prevAssignedToUser = prevTicket.getAssignedTo();
            Employee prevAssignedByUser = prevTicket.getAssignedBy();

            EmployeeDto assignedToUser = employeeService.getEmployeeById(ticketDto.getAssignedToUser());
            EmployeeDto assignedByUser = employeeService.getEmployeeById(ticketDto.getAssignedById());
            prevTicket.setAssignedTo(modelMapper.map(assignedToUser, Employee.class));
            prevTicket.setAssignedBy(modelMapper.map(assignedByUser, Employee.class));
            Ticket updatedTicket = ticketRepository.save(prevTicket);

            ticketHistoryService.updateTicketHistoryEndDate(prevAssignedToUser, prevAssignedByUser, updatedTicket.getId());

            ticketHistoryService.saveTicketHistory(convertToDto(updatedTicket));
            return convertToDto(updatedTicket);
        }
        else {
            throw new ResourceNotFoundException("Ticket Not found with this id"+id);
        }
    }

    public TicketDto convertToDtoWithAttachment(Ticket ticket, Attachment file){
        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(ticket.getId());
        ticketDto.setSummary(ticket.getSummary());
        ticketDto.setDescription(ticket.getDescription());
        ticketDto.setFile(null); // Set to null if not needed
        ticketDto.setFileId(ticket.getFileId());
        ticketDto.setFilename(file.getFileName());
        ticketDto.setContentType(file.getContentType());
        ticketDto.setSize(file.getSize());
//        ticketDto.setContent(file.getContent());
        ticketDto.setCreatedById(ticket.getCreatedBy().getId());
        ticketDto.setCreatedDateTime(ticket.getCreatedDateTime());
        ticketDto.setDepartmentId(ticket.getDepartment().getId());
        ticketDto.setDepartmentName(ticket.getDepartment().getName());

        if (ticket.getTeam() != null && ticket.getTeamHead() != null) {
            ticketDto.setTeamId(ticket.getTeam().getId());
            ticketDto.setTeamName(ticket.getTeam().getName());
            ticketDto.setTeamHeadName(ticket.getTeamHead().getUser().getName());
            ticketDto.setTeamHeadId(ticket.getTeamHead().getUser().getId());
        }else{
            ticketDto.setTeamId(null);
            ticketDto.setTeamName(null);
            ticketDto.setTeamHeadName(null);
            ticketDto.setTeamHeadId(null);
        }

        ticketDto.setDepartmentHeadName(ticket.getDepartmentHead().getUser().getName());
        ticketDto.setDepartmentHeadId(ticket.getDepartmentHead().getUser().getId());
        ticketDto.setTicketType(ticket.getTicketType());
        ticketDto.setAssignedToUser(ticket.getAssignedTo().getId());
        ticketDto.setPriority(ticket.getPrirority());
        ticketDto.setStatus(ticket.getStatus());
        ticketDto.setTargetResolutionDate(ticket.getTargetResolutionDate());
        ticketDto.setActualResolutionDate(ticket.getActualResolutionDate());
        ticketDto.setResolutionSummary(ticket.getResolutionSummary());

        return ticketDto;
    }

    public TicketDto convertToDto(Ticket ticket) {
//        return modelMapper.map(ticket, TicketDto.class);
        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(ticket.getId());
        ticketDto.setSummary(ticket.getSummary());
        ticketDto.setDescription(ticket.getDescription());
        ticketDto.setFileId(ticket.getFileId());
        ticketDto.setCreatedById(ticket.getCreatedBy().getId());
        ticketDto.setCreatedDateTime(ticket.getCreatedDateTime());
        ticketDto.setDepartmentId(ticket.getDepartment().getId());
        ticketDto.setDepartmentName(ticket.getDepartment().getName());

        ticketDto.setDepartmentHeadName(ticket.getDepartmentHead().getName());
        ticketDto.setDepartmentHeadId(ticket.getDepartmentHead().getId());
        if(ticket.getTeamHead() != null && ticket.getTeam() != null){
            ticketDto.setTeamId(ticket.getTeam().getId());
            ticketDto.setTeamName(ticket.getTeam().getName());

            ticketDto.setTeamHeadName(ticket.getTeamHead().getName());
            ticketDto.setTeamHeadId(ticket.getTeamHead().getId());
        }

        ticketDto.setTicketType(ticket.getTicketType());
        ticketDto.setAssignedToUser(ticket.getAssignedTo().getId());
        ticketDto.setAssignedById(ticket.getAssignedBy().getId());
        ticketDto.setPriority(ticket.getPrirority());
        ticketDto.setStatus(ticket.getStatus());
        ticketDto.setTargetResolutionDate(ticket.getTargetResolutionDate());
        ticketDto.setActualResolutionDate(ticket.getActualResolutionDate());
        ticketDto.setResolutionSummary(ticket.getResolutionSummary());
        return ticketDto;
    }


    public Ticket convertToEntity(TicketDto ticketDto) {
        return modelMapper.map(ticketDto, Ticket.class);
    }
}
