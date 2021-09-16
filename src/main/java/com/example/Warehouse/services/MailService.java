package com.example.Warehouse.services;

import com.example.Warehouse.pojo.Mail;

public interface MailService {
    public void sendEmail(Mail mail,String fileToAttach);

	public void sendEmailPassword(Mail mail);
}
