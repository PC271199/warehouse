package com.example.Warehouse.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.Warehouse.dtos.scheduleService.ScheduleBukkenUserDto;
import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.bukkenService.Bukken;
import com.example.Warehouse.entities.bukkenService.InterestedBukken;
import com.example.Warehouse.entities.scheduleService.EventSchedule;
import com.example.Warehouse.entities.scheduleService.ScheduleBukken;
import com.example.Warehouse.entities.scheduleService.ScheduleBukkenUser;
import com.example.Warehouse.entities.scheduleService.Target;
import com.example.Warehouse.entities.scheduleService.Target2;
import com.example.Warehouse.exceptions.accountService.EmptyException;
import com.example.Warehouse.exceptions.common.NullException;
import com.example.Warehouse.mapper.ScheduleBukkenUserMapper;
import com.example.Warehouse.pojo.Mail;
import com.example.Warehouse.repositories.accountService.AccountRepository;
import com.example.Warehouse.repositories.scheduleService.EventScheduleRepository;
import com.example.Warehouse.repositories.scheduleService.ScheduleBukkenRepository;
import com.example.Warehouse.repositories.scheduleService.ScheduleBukkenUserRepository;

@Service
public class ScheduleService {
	private static final String MAIL_ADMIN = "conglp2mendix@gmail.com";
	@Autowired
	private ScheduleBukkenRepository scheduleBukkenRepo;

	@Autowired
	private ScheduleBukkenUserRepository scheduleUserRepo;

	@Autowired
	private AccountRepository accRepo;

	@Autowired
	private EventScheduleRepository eventRepo;

	@Autowired
	private BukkenService bukkenService;

	@Autowired
	MailServiceImpl mailServiceImpl;

	public ScheduleBukken DS_getScheduleBukken(int bukkenId) {
		Bukken thisBukken = bukkenService.getById(bukkenId);
		if (thisBukken.getScheduleBukken() != null) {
			return thisBukken.getScheduleBukken();
		} else {
			ScheduleBukken newScheduleBukken = new ScheduleBukken();
			newScheduleBukken.setBukken(thisBukken);
			scheduleBukkenRepo.save(newScheduleBukken);
			return newScheduleBukken;
		}
	}

	public ScheduleBukken saveScheduleBukken(ScheduleBukken scheduleBukken) {
		Optional<ScheduleBukken> thisScheduleBukken = scheduleBukkenRepo.findById(scheduleBukken.getId());
		ScheduleBukken result = thisScheduleBukken.get();
		Bukken thisBukken = result.getBukken();
		if (thisBukken != null) {
			thisBukken.setConfigured(true);
		}
		bukkenService.save(thisBukken);
		result.setConfigured(true);
		result.setDates(scheduleBukken.getDates());
		result.setStartDate(scheduleBukken.getStartDate());
		result.setEndDate(scheduleBukken.getEndDate());
		result.setStartTime(scheduleBukken.getStartTime());
		result.setEndTime(scheduleBukken.getEndTime());
		result.setNote(scheduleBukken.getNote());
		scheduleBukkenRepo.save(result);
		return scheduleBukken;
	}

	// get scheduleBukkenUser by id
	public ScheduleBukkenUser getScheduleBukkenUserById(int id) {
		Optional<ScheduleBukkenUser> thisScheduleBukkenUser = scheduleUserRepo.findById(id);
		if (thisScheduleBukkenUser.isPresent()) {
			return thisScheduleBukkenUser.get();
		} else {
			throw new NullException();
		}
	}

	// get scheduleBukken by id
	public ScheduleBukken getScheduleBukkenById(int id) {
		Optional<ScheduleBukken> thisScheduleBukken = scheduleBukkenRepo.findById(id);
		if (thisScheduleBukken.isPresent()) {
			return thisScheduleBukken.get();
		} else {
			throw new NullException();
		}
	}

	// create scheduleBukkenUser
	public ScheduleBukkenUser createScheduleBukkenUser(int scheduleBukkenId) {
		ScheduleBukken thisScheduleBukken = getScheduleBukkenById(scheduleBukkenId);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Account> thisAccount = accRepo.findByEmail(authentication.getName());
		if (thisAccount.isPresent() == false) {
			throw new NullException();
		}
		ScheduleBukkenUser result = new ScheduleBukkenUser();
		result.setStatusId(0);
		result.setStatusName("Initial");
		result.setBukkenName(thisScheduleBukken.getBukken().getName());
		result.setAccount(thisAccount.get());
		result.setSchedulebukken(thisScheduleBukken);
		scheduleUserRepo.save(result);
		return result;
	}

	// get All scheduleBukkenUser
	public List<ScheduleBukkenUser> getAllScheduleBukkenUser() {
		return scheduleUserRepo.findAll();
	}

	// get ScheduleBukken By ScheduleBukkenUserId
	public ScheduleBukken getScheduleBukken_ByScheduleBukkenUser(int scheduleBukkenUserId) {
		ScheduleBukkenUser scheduleBukkenUser = getScheduleBukkenUserById(scheduleBukkenUserId);
		return scheduleBukkenUser.getSchedulebukken();
	}

	// get all ScheduleBukkenUser by page
	public Page<ScheduleBukkenUser> getAllByPage(int pageIndex) {
		Pageable pageable = PageRequest.of(pageIndex, 10, Sort.by("create_At"));
		Page<ScheduleBukkenUser> result = scheduleUserRepo.findAllAtAdminPage(pageable);
		if (result != null && result.getSize() > 0) {
			System.out.println();
			return result;
		} else {
			throw new EmptyException();
		}
	}

	// count All ScheduleBukkenUser belong bukken
	public long countAllByBukken(int bukkenId) {
		Bukken thisBukken = bukkenService.getById(bukkenId);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Account> thisAccount = accRepo.findByEmail(authentication.getName());
		if (thisAccount.isPresent() == false) {
			throw new NullException();
		}
		Account account = thisAccount.get();
		if (thisBukken.getScheduleBukken() != null) {
			return scheduleUserRepo.findAll().stream()
					.filter(element -> element.getSchedulebukken().getId() == thisBukken.getScheduleBukken().getId()
							&& element.getAccount().getId() == account.getId())
					.count();
		} else
			return 0;
	}

	// count All ScheduleBukkenUser belong account
	public long countAllByAccount() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Account> thisAccount = accRepo.findByEmail(authentication.getName());
		if (thisAccount.isPresent() == false) {
			throw new NullException();
		}
		Account account = thisAccount.get();
		return scheduleUserRepo.countByAccountId(account.getId());
	}

	// get bukken by schedule bukken user id
	public Bukken getBukkenByScheduleBukkenUserId(int scheduleBukkenUserId) {
		ScheduleBukkenUser thisScheduleBukkenUser = getScheduleBukkenUserById(scheduleBukkenUserId);
		int bukkenId = scheduleBukkenRepo.getBukkenIdByScheduleBukkenId(thisScheduleBukkenUser.getSchedulebukken().getId());
//		Bukken result = bukkenService.getById(bukkenId);
		Bukken result = thisScheduleBukkenUser.getSchedulebukken().getBukken();
		return result;
	}

	// count All ScheduleBukkenUser with status id >= 1
	public long countAll() {
		return scheduleUserRepo.findAll().stream().filter(element -> element.getStatusId() >= 1).count();
	}

	// save ScheduleBukkenUser
	public ScheduleBukkenUser saveScheduleBukkenUser(ScheduleBukkenUser scheduleBukkenUser) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentMail = authentication.getName();
		Set<EventSchedule> eventSchedules = scheduleBukkenUser.getEventScheduleList();
		ScheduleBukkenUser scheduleBukkenUserDB = scheduleUserRepo.findById(scheduleBukkenUser.getId()).get();
		if (scheduleBukkenUserDB.getEventScheduleList() == null
				|| scheduleBukkenUserDB.getEventScheduleList().size() == 0) {
			for (EventSchedule eventSchedule : eventSchedules) {
				eventSchedule.setSchedulebukkenuser(scheduleBukkenUserDB);
			}
			eventRepo.saveAll(eventSchedules);
		} else {
			for (EventSchedule eventSchedule : eventSchedules) {
				if (eventRepo.findById(eventSchedule.getId()).isPresent()) {
					EventSchedule eventScheduleDB = eventRepo.findById(eventSchedule.getId()).get();
					eventScheduleDB.setStartDate(eventSchedule.getStartDate());
					eventScheduleDB.setStartTime(eventSchedule.getStartTime());
					eventScheduleDB.setEndTime(eventSchedule.getEndTime());
					eventRepo.save(eventScheduleDB);
				}
			}
		}
		if (scheduleBukkenUser.getTarget() != null) {
			if (scheduleBukkenUser.getTarget().toString().equals("EstablishNew")) {
				scheduleBukkenUserDB.setTarget(Target.EstablishNew);
			}
			if (scheduleBukkenUser.getTarget().toString().equals("Move")) {
				scheduleBukkenUserDB.setTarget(Target.Move);
			}
			if (scheduleBukkenUser.getTarget().toString().equals("Expand")) {
				scheduleBukkenUserDB.setTarget(Target.Expand);
			}
		}
		if (scheduleBukkenUser.getTarget2() != null) {
			if (scheduleBukkenUser.getTarget2().toString().equals("JustConsider")) {
				scheduleBukkenUserDB.setTarget2(Target2.JustConsider);
			}
			if (scheduleBukkenUser.getTarget2().toString().equals("CollectInfor")) {
				scheduleBukkenUserDB.setTarget2(Target2.CollectInfor);
			}
			if (scheduleBukkenUser.getTarget2().toString().equals("CompareWarehouse")) {
				scheduleBukkenUserDB.setTarget2(Target2.CompareWarehouse);
			}
		}
		scheduleBukkenUserDB.setNote(scheduleBukkenUser.getNote());
		scheduleBukkenUserDB.setStatusId(1);
		scheduleBukkenUserDB.setStatusName("Waiting for approve");
		Mail mail = new Mail();

		mail.setMailFrom("phuoccong99@gmail.com");// comment replace
		mail.setMailTo(MAIL_ADMIN);
		mail.setMailContent(currentMail + " has booked " + scheduleBukkenUserDB.getBukkenName());

		try {
			MailService mailService = (MailService) mailServiceImpl;
			mailService.sendEmailPassword(mail);
		} catch (Exception e) {
			throw e;
		}
		return scheduleUserRepo.save(scheduleBukkenUserDB);
	}

	// forward ScheduleBukkenUser
	public ScheduleBukkenUser forwardScheduleBukkenUser(ScheduleBukkenUser scheduleBukkenUser) {
		Set<EventSchedule> eventSchedules = scheduleBukkenUser.getEventScheduleList();
		ScheduleBukkenUser scheduleBukkenUserDB = scheduleUserRepo.findById(scheduleBukkenUser.getId()).get();
		Account thisAccount = scheduleBukkenUserDB.getAccount();
		for (EventSchedule eventSchedule : eventSchedules) {
			if (eventRepo.findById(eventSchedule.getId()).isPresent()) {
				EventSchedule eventScheduleDB = eventRepo.findById(eventSchedule.getId()).get();
				eventScheduleDB.setDecision(eventSchedule.isDecision());
				eventRepo.save(eventScheduleDB);
			}
		}
		scheduleBukkenUserDB.setStatusId(2);
		scheduleBukkenUserDB.setStatusName("Success");
		String mailOwner = bukkenService.getMailOwner(scheduleBukkenUserDB.getSchedulebukken().getBukken().getId());
		Mail mail = new Mail();

		mail.setMailFrom(MAIL_ADMIN);
		mail.setMailTo(mailOwner);
		mail.setMailContent(
				thisAccount.getEmail() + " has booked " + scheduleBukkenUserDB.getBukkenName() + " successfully");

		Mail mail2 = new Mail();

		mail2.setMailFrom(MAIL_ADMIN);
		mail2.setMailTo(scheduleBukkenUser.getAccount().getEmail().toString());
		System.out.println(scheduleBukkenUser.getAccount().getEmail());
		mail2.setMailContent(
				thisAccount.getEmail() + " has booked " + scheduleBukkenUserDB.getBukkenName() + " successfully");

		try {
			MailService mailService = (MailService) mailServiceImpl;
			MailService mailService2 = (MailService) mailServiceImpl;
			mailService.sendEmailPassword(mail);
			mailService2.sendEmailPassword(mail2);
		} catch (Exception e) {
			throw e;
		}
		return scheduleUserRepo.save(scheduleBukkenUserDB);
	}

	// get scheduleBukkenUser by Account per page belong bukken
	public Page<ScheduleBukkenUser> getScheduleBukkenUser_ByAccount_ByBukken(int bukkenId, int pageIndex) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Account> thisAccount = accRepo.findByEmail(authentication.getName());
		if (thisAccount.isPresent() == false) {
			throw new NullException();
		}
		Account account = thisAccount.get();
		Bukken thisBukken = bukkenService.getById(bukkenId);
		if (thisBukken.getScheduleBukken() != null) {
			return scheduleUserRepo.findByAccountIdBukkenId(PageRequest.of(pageIndex, 10), account.getId(),
					thisBukken.getScheduleBukken().getId());
		} else
			return null;
	}

	// get scheduleBukkenUser by Account per page
	public Page<ScheduleBukkenUser> getScheduleBukkenUser_ByAccount(int pageIndex) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Account> thisAccount = accRepo.findByEmail(authentication.getName());
		if (thisAccount.isPresent() == false) {
			throw new NullException();
		}
		Account account = thisAccount.get();
		return scheduleUserRepo.findByAccountId(PageRequest.of(pageIndex, 10), account.getId());
	}

	// get scheduleBukkenUser belong to ownerId
	public Page<ScheduleBukkenUser> getScheduleBukkenUser_BelongOwner(int pageIndex, int ownerId) {
		Pageable pageable = PageRequest.of(pageIndex, 10);
		Optional<Account> thisAccount = accRepo.findById(ownerId);
		if (thisAccount.isPresent() == false) {
			throw new NullException();
		}
		Account thisOwner = thisAccount.get();
		Set<Bukken> bukkenList = thisOwner.getBukkens();
		List<ScheduleBukkenUser> scheduleBukkenUserListResult = new ArrayList<ScheduleBukkenUser>();
		for (Bukken bukken : bukkenList) {
			ScheduleBukken scheduleBukken = bukken.getScheduleBukken();
			if (scheduleBukken != null) {
				if (scheduleBukken.getScheduleBukkenUserList() != null) {
					List<ScheduleBukkenUser> scheduleBukkenUserList = scheduleBukken.getScheduleBukkenUserList()
							.stream().filter(element -> element.getStatusId() > 1).collect(Collectors.toList());
					scheduleBukkenUserListResult.addAll(scheduleBukkenUserList);
				}
			}
		}
		Page<ScheduleBukkenUser> finalResult = new PageImpl<>(scheduleBukkenUserListResult, pageable,
				scheduleBukkenUserListResult.size());
		return finalResult;
	}

	// count scheduleBukkenUser belong to ownerId with statusId =2
	public long countScheduleBukkenUser_BelongOwner(int ownerId) {
		Optional<Account> thisAccount = accRepo.findById(ownerId);
		if (thisAccount.isPresent() == false) {
			throw new NullException();
		}
		Account thisOwner = thisAccount.get();
		Set<Bukken> bukkenList = thisOwner.getBukkens();
		List<ScheduleBukkenUser> scheduleBukkenUserListResult = new ArrayList<ScheduleBukkenUser>();
		for (Bukken bukken : bukkenList) {
			ScheduleBukken scheduleBukken = bukken.getScheduleBukken();
			if (scheduleBukken != null) {
				Set<ScheduleBukkenUser> scheduleBukkenUserList = scheduleBukken.getScheduleBukkenUserList();
				scheduleBukkenUserListResult.addAll(scheduleBukkenUserList);
			}
		}
		return scheduleBukkenUserListResult.stream().filter(element -> element.getStatusId() > 1).count();
	}

	// delete all scheduleBukkenUser
	public void deleteAllScheduleBukkenUser() {
		scheduleUserRepo.deleteAll();
	}

	// delete all scheduleBukkenUser
	public void deleteAllScheduleBukken() {
		scheduleBukkenRepo.deleteAll();
	}
}
