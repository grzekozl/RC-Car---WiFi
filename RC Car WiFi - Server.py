import socket               # Import socket module
import RPi.GPIO as GPIO
import os
import time

time.sleep(20)

setup = True

soc = socket.socket()         # Create a socket object
host = "192.168.1.5"     # Get local machine IP (set to static)
port = 2004                # Reserve a port for your service.
soc.bind((host, port))       # Bind to the port
soc.listen(5)                 # Now wait for client connection.
conn, addr = soc.accept() #Nawiazywanie polaczenia z jednym klientem
print ("Polaczono sie z: ", addr)

while True:
#	conn, addr = soc.accept()     # Establish connection with client.
	msg = conn.recv(1024)


	if not msg or msg == 'exit':
		conn.close() # Zamykanie starego polaczenia

		# Upewnianie sie ze GPIO jest wylaczone po zamknieciu polaczenia
		GPIO.output(3, GPIO.HIGH)
		GPIO.output(5, GPIO.HIGH)
		GPIO.output(7, GPIO.HIGH)
		GPIO.output(11, GPIO.HIGH)
		GPIO.cleanup()

		# Oczekiwanie na nowe polaczenie
		conn,addr = soc.accept()
		print("Polaczono sie z: ", addr)
		setup = True
	else:
		if setup:
			#GPIO setup
			GPIO.setmode(GPIO.BOARD)

			GPIO.setup(3, GPIO.OUT)
			GPIO.setup(5, GPIO.OUT)
			GPIO.setup(7, GPIO.OUT)
			GPIO.setup(11, GPIO.OUT)

			GPIO.output(3, GPIO.HIGH)
			GPIO.output(5, GPIO.HIGH)
			GPIO.output(7, GPIO.HIGH)
			GPIO.output(11, GPIO.HIGH)
			setup = False

		msg = msg[2:] #Trzeba porownywac bez dwoch pierwszych znakow
		print(msg)

		if(msg == 'Pw'):
			GPIO.output(3, GPIO.LOW)

		if(msg == 'Pa'):
			GPIO.output(11, GPIO.LOW)

		if(msg == 'Ps'):
			GPIO.output(5, GPIO.LOW)

		if(msg == 'Pd'):
			GPIO.output(7, GPIO.LOW)



		if(msg == 'Rw'):
			GPIO.output(3, GPIO.HIGH)

		if(msg == 'Ra'):
			GPIO.output(11, GPIO.HIGH)

		if(msg == 'Rs'):
			GPIO.output(5, GPIO.HIGH)

		if(msg == 'Rd'):
			GPIO.output(7, GPIO.HIGH)
