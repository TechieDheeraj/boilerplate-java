package com.beta.replyservice;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class ReplyController {

  private String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  private String digest(String input) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException(e);
    }

    byte[] byteInput = input.getBytes();
    return bytesToHex(md.digest(byteInput));
  }

  @GetMapping("/reply")
  public ReplyMessage replying() {
    return new ReplyMessage("Message is empty");
  }

  @GetMapping("/reply/{message}")
  public ReplyMessage replying(@PathVariable String message) {
    return new ReplyMessage(message);
  }

  @GetMapping("/v2/reply")
  public ReplyMessage replyingNew() {
    return new ReplyMessage("Message is empty");
  }

  @GetMapping("/v2/reply/{message}")
  public ReplyMessage replyingNew(@PathVariable String message) {
    String arr[] = message.split("-");

    if (arr.length != 2) {
      return new ReplyMessage("Enter Valid Input: " + message);
    }

    String input = arr[1]; // Reading string
    char[] num = arr[0].toCharArray(); // Reading Operations
    for(char c : num) {
      if (c == '1') {
        StringBuilder sb = new StringBuilder(input);
        sb.reverse();
        input = sb.toString();
      } else {
        input = this.digest(input);
      }
    }
    return new ReplyMessage(input);
  }
}
