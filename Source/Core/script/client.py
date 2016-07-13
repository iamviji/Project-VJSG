import socket
import sys
import time
UDP_IP = "127.0.0.1"
UDP_PORT = 7000 
MESSAGE = "Hello, World! VI"

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) # UDP

with open (sys.argv[1], "r") as f:
	for line in f:
		linex = line.rstrip ('\n');
		if linex == "TT":
			print ("sleeping\n")
			time.sleep (1)
		else:
			s.sendto(line.encode (), (UDP_IP, UDP_PORT))



