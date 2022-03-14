# spring-boot-ajax

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
        # zablokujeme turniket pre vsetky monitory ktore cakaju
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


if __name__ == '__main__':
    main()
```
