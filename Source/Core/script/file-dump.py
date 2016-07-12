import socket
import sys
UDP_IP = "0.0.0.0"
UDP_PORT = 6000 

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) # UDP
server_addr = (UDP_IP, UDP_PORT)
s.bind(server_addr)
f = open (sys.argv[1], 'w')
var = 1
while var == 1:
	data,addr = s.recvfrom(1024)
	while(data):
		f.write(data)
		print (data)
		data,addr = s.recvfrom(1024)
		f.flush ()
