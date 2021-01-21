package progWeb.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import model.Character;
import model.Universe;

@Controller
public class MyController {

    @RequestMapping(value = "/helloWorld", method = RequestMethod.GET)
    public void myFirstEndPoint(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {
        System.out.println("Hello Console");
        response.getOutputStream().write("Hello".getBytes("UTF-8"));
    }

    @RequestMapping(value = "/helloWorld", method = RequestMethod.POST)
    public void myPostEndPoint(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {
        System.out.println("Bonjour Console");
        response.getOutputStream().write("bonjour".getBytes("UTF-8"));
    }
    
    @RequestMapping(value = "/helloWorldRedirect", method = RequestMethod.POST)
    public void redirectToHello(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {
        response.sendRedirect("/helloWorld");
    }

    @RequestMapping(value = "/param", method = RequestMethod.GET)
    public void showParamFromGet(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {
        Map<String, String[]> param = request.getParameterMap();
        for (Object key : param.keySet()) {
            System.out.println("" + key + " : " + param.get(key)[0]);
        }
    }
    
    @RequestMapping(value = "/param", method = RequestMethod.POST)
    public void showParamFromPost(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {
        Map<String, String[]> param = request.getParameterMap();
        for (Object key : param.keySet()) {
            System.out.println("" + key + " : " + param.get(key)[0]);
        }
    }

    @RequestMapping(value = "/character", method = RequestMethod.POST)
    public void choiceCharacter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String charName = request.getParameter("name");
        Character chosen = null;
        Character chosenmonster = null;

        for (Character c : Universe.getCharacters()) {
            if (c.getName().equals(charName)) {
                chosen = c;
            }
        }
        if (chosen != null) {
            response.setHeader("HP", "" + chosen.getHpMax());
            response.setHeader("attack", "" + chosen.getAttack());
            Cookie cookieName = new Cookie("name", charName);
            response.addCookie(cookieName);
            response.addCookie(new Cookie("HP", "" + chosen.getHpMax()));
            response.addCookie(new Cookie("attack", "" + chosen.getAttack()));
            response.addCookie(new Cookie("dodge", "" + chosen.getDodgeProbability()));
            
            chosenmonster=Universe.getMonsters().get(0);
            response.addCookie(new Cookie("monsterId", "0"));
            response.addCookie(new Cookie("nameMonster", "" + chosenmonster.getName()));
            response.addCookie(new Cookie("HPMonster", "" + chosenmonster.getHpMax()));
            response.addCookie(new Cookie("attackMonster", "" + chosenmonster.getAttack()));
            response.addCookie(new Cookie("dodgeMonster", "" + chosenmonster.getDodgeProbability()));

        }
        response.sendRedirect("/showCharacter.html");
    }

    @RequestMapping(value = "/takeDammage", method = RequestMethod.GET)
    public void takeDammage(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {

        String damStr = request.getParameter("dammage");
        System.out.println(damStr);
        if (damStr != null) {
            int dammage = Integer.parseInt(damStr);
            int hp = 0;
            for (Cookie c : request.getCookies()) {

                if (c.getName().equals("HP")) {
                    hp = Integer.parseInt(c.getValue());
                }
            }
            hp = (hp - dammage > 0) ? hp - dammage : 0;
            response.addCookie(new Cookie("HP", "" + hp));
        }
        response.sendRedirect("/showCharacter.html");

    }

    @RequestMapping(value = "/nextFoe", method = RequestMethod.GET)
    public void nextFoe(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {
        int previousFoe = -1;
        for (Cookie c : request.getCookies()) {
            if (c.getName().equals("foeNumber")) {
                previousFoe = Integer.parseInt(c.getValue());
            }
        }
        try {
            Character foe = Universe.getMonsters().get(previousFoe + 1);
            response.addCookie(new Cookie("foeNumber", "" + (previousFoe + 1)));
            response.addCookie(new Cookie("foeName", foe.getName()));
            response.addCookie(new Cookie("foeHP", " " + foe.getHpMax()));
            response.addCookie(new Cookie("foeAttack", "" + foe.getAttack()));
        } catch (IndexOutOfBoundsException e) {
            response.getOutputStream().write("Tous les ennemis sont vaincus".getBytes("UTF-8"));
        }
    }

}

