# Paralelné programovanie a distribuované systémy 2022

## Cvičenie 4, zadanie [Atómová elektráreň #2](https://uim.fei.stuba.sk/i-ppds/4-cvicenie-vecerajuci-filozofi-atomova-elektraren-🍽%EF%B8%8F/)

### **1. Urobte analýzu, o aké typy synchronizačných úloh (prípadne ich modifikácie či kombinácie) sa v tejto úlohe jedná.**

V našej úlohe sa jedná o zosynchronizovanie prístupu 3 čidiel a 8 monitorov k jednému úložisku do ktorého čidlá zapisujú hodnoty a monitory ich z neho čítajú a zobrazujú. Musíme vykonať správne synchronizačné úkony aby sa kód vykonával paralelne a aby monitory mali na úvod všetky hodnoty z čidiel, aby mali čo prečítať a z toho dôvodu musíme zabezpečiť aby nám v prvej iterácií najprv čidla zapisovali a až potom monitory mohli čítať z úložiska.

### **2. Presne namapujte vami zvolené synchronizačné úlohy (primitívy) na jednotlivé časti zadania.**

Ako som už spomenul vyššie, musíme sa zamerať na prístup do spoločnej pamäte a správne a plynule čítanie a zapisovanie do nej. 
Dôležité pravidlá musíme dodržať už hneď pri prvej iterácií kde je dôležité aby začali do úložiska zapisovať najprv všetky čidlá a až keď zapíšu všetky hodnoty tak môžu začať monitory čítať. Toto správanie sme ošetrili vyvolaním wait na Evente na monitoroch. Čidlá vykonajú aktualizáciu ktorá trvá od 50 do 60ms a môžu začať prechádzať cez turniket a následne zamknúť spoločné úložisko s tým že dalšie čidla majú do neho prístup, toto úložisko zamkneme pomocou prepínaču. Máme 3 druhy čidiel a jedno z nich na dlhší čas aktualizácie ako zvyšné dve, 20 až 25ms zatiaľ čo zvyšné dve len 10 až 20ms. Následne prvé čidlo vyvolá signál na Event pre monitory, no monitory musia stále čakať na prístup do úložiska pretože úložisko nám v prepínači odomkne až posledné čidlo. Monitory môžu teraz bežpečne prečítať údaje z úložiska. Ďalej si môžeme všimnúť že čidlá aj monitory majú náhodnu dobu aktualizácie a to je dôvod prečo neskôr môžeme vidieť že nebude zapisaovaíť aj čítať 8 monitorov a 3 čidlá naraz, támo n ale nevadí.

### **3. Napíšte pseudokód riešenia úlohy.**

```
class Shared:
    def __init__(self):
        self.access_data = Semaphore(1)
        self.turnstile = Semaphore(1)
        self.ls_monitor = Lightswitch()
        self.ls_sensor = Lightswitch()
        self.valid_data = Event()


class Lightswitch:
    def __init__(self):
        self.mutex = Mutex()
        self.counter = 0

    def lock(self, sem):
        self.mutex.lock()
        counter = self.counter
        self.counter += 1
        if self.counter je rovný 1:
            sem.wait()
        self.mutex.unlock()
        return counter

    def unlock(self, sem):
        self.mutex.lock()
        self.counter -= 1
        # podmienku nizsie zavola len posledny objekt ktory prišiel do Lightswitchu
        if self.counter je rovný 0:
            sem.signal()  
        self.mutex.unlock()


def monitor(monitor_id, shared):
    # zabezpečuje nám aby monitory nečítali dáta pred zápismi senzorov v úvode
    shared.valid_data.wait() 

    while True:
        # vykonanie aktualizácie ktorá trvá 40 až 50ms
        sleep(randint(40 az 50ms)
        # zablokujeme turniket pre všetky monitory ktoré čakajú
        shared.turnstile.wait()
        # ak posledný senzor odomkol lightswitchom úložisko, monitor si ho môže zamknúť
        monitor_counter = shared.ls_monitor.lock(shared.access_data)
        shared.turnstile.signal()

        print(monitor {monitor_id}: počet_čítajúcich_monitorov={monitor_counter})
        # odchádzame z úložiska a nechávame ho voľné
        shared.ls_monitor.unlock(shared.access_data)


def sensor(sensor_id, shared):
    while True:
        # vykonanie aktualizácie ktorá trvá 50 až 60ms
        sleep(randint(50 až 60ms)

        # čidlá prechádzajú cez turniket, pokým ho nezamkne monitor
        shared.turnstile.wait()
        shared.turnstile.signal()

        # prvé čidlo zamkne úložisko, všetky 3 čidlá môžu ísť ďalej a majú ku nemu prístup
        sensor_counter = shared.ls_sensor.lock(shared.access_data)

        #  tretie čidlo ma v priemere dlhší časť aktualizácie
        if sensor_id rovná sa 2:
            dlzka_aktualizacie = randint(20 až 25ms)
        else sensor_id sa nerovná 2:
            dlzka_aktualizacie = randint(10 až 20ms)

        print(čidlo {sensor_id}: počet_zapisujúcich_čidiel={sensor_counter}, trvanie_zapisu={dlzka_aktualizacie}')
        sleep(dlzka_aktualizacie)

        # signal pre monitpry nám vyvolá už prvé čidlo
        shared.valid_data.signal() 
        # pamäť nám v lightswitchi odomkne až posledné čidlo, 
        # z toho dôvodu v uvodnej iteracii mame v našom prípade garanciu že všetky 3 budú pred monitormi
        shared.ls_sensor.unlock(shared.access_data)



def main():
    shared = Shared()
    monitors = [Thread(monitor, i+1, shared) for i in range(8)]
    sensors = [Thread(sensor, j+1, shared) for j in range(3)]

    for thread in monitors + sensors:
        thread.join()
```
