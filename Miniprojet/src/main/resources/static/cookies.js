function findValueCookie(key) {
    var keyCookie = document.cookie
        .split(';')
        .find(row => row.trim().startsWith(key + "="));
    if (keyCookie != null) {
        return keyCookie.split('=')[1];
    }
};

function start() {
    var hpmaxMonster = parseFloat(findValueCookie("HPMonster"));
    var dodgeMonster = parseFloat(findValueCookie("dodgeMonster"));
    var attackMonster = parseFloat(findValueCookie("attackMonster"))
    var randproba = 0;

    while (hpmax > 0 && hpmaxMonster > 0) {
        randproba = Math.random();
        if (dodge < randproba) {
            hpmax -= attackMonster;
            hpmax < 0 ? hpmax = 0 : hpmax = hpmax;
            voyantP(1);
            document.getElementById("HPValue").innerHTML = hpmax;
        } else {
            voyantP(0);
        }
        if (dodgeMonster < randproba) {
            hpmaxMonster -= attack;
            hpmaxMonster < 0 ? hpmaxMonster = 0 : hpmaxMonster = hpmaxMonster;
            voyantMonster(1);
            document.getElementById("HPValueMonster").innerHTML = hpmaxMonster;
        } else {
            voyantMonster(0);
        }
    }
    if (hpmax == 0) {
        document.getElementById("spanmon").innerHTML = nbtue;
        document.getElementById("defeat").style.display = "initial";
        document.getElementById("fightbtn").disabled = true;
        document.getElementById("nextbtn").disabled = true;
    } else {
        nbtue++;
        document.getElementById("nextbtn").style.display = "inital";
    }
}

function loadnext() {
    var ajax = new XMLHttpRequest();
    ajax.open('POST', 'netfighter');
    ajax.onload = function() {
        if (ajax.status === 200) {
            var reponse = ajax.responseText;
            console.log(ajax.responseText);
            if (reponse == "Au suivant!") {
                chargeinfomonstre();
                document.getElementById("nextbtn").style.display = "none";
            } else {
                document.getElementById("fightbtn").disabled = true;
                document.getElementById("nextbtn").disabled = true;
                document.getElementById("spanmon").innerHTML = nbtue;
                document.getElementById("defeat").style.display = "initial";
            }

            alert(ajax.responseText);

        } else {
            console.log('ajax failed');
        }
    };
    ajax.send();
};