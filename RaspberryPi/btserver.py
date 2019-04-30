from bluedot.btcomm import BluetoothServer
from signal import pause
import detection
import time

primaryName = ""
primaryNumber = ""
secondaryNumber = ""

def data_received(data):
    
    global primaryName
    global primaryNumber
    global secondaryNumber
    
    stringData = 'Received Data: ' + data
    print(stringData)
    
    # s.send(stringData + '\n') 
    
    data = data.split(',')
    
    if(data[0] == 'contact'):
        primaryName, primaryNumber, secondaryNumber = contact(data)
        print(primaryName, primaryNumber, secondaryNumber) 
    elif(data[0] == 'detect'):
        alerts = detect(primaryName, primaryNumber, secondaryNumber,data)
        
        stringCmd = ','.join(alerts)
        print( "Sending: " + stringCmd )
        s.send( stringCmd + '\n' )
        
    #elif data[0]=='stop':
     #   stop(data)
    

def contact(data):
    command, primaryName, primaryNumber, secondaryNumber = data
    stringCmd = 'Command: ' + command
    string0 = 'Primary Name: ' + primaryName
    string1 = 'Primary Number: ' + primaryNumber
    string2 = 'Secondary Number: ' + secondaryNumber
    print(stringCmd)
    print(string0)
    print(string1)
    print(string2)
    
    return [primaryName, primaryNumber, secondaryNumber]
    
def detect(primaryName, primaryNumber, secondaryNumber, data):
    command = data[0]
    tmax = data[1]
    tmin = data[2]
    stringCmd = 'Command: ' + command
    stringHMax = 'HMax: ' + tmax
    stringHMin = 'HMin: ' + tmin
    print(stringCmd)
    print(stringHMax)
    print(stringHMin)
    
    if (tmax == '' or tmin == ''):
        tmax = '30'
        tmin = '29'
    
    #detection.detect('Jordan','8329300230','9152273680', int(tmax), int(tmin))
    # return detection.detect(primaryName, primaryNumber, secondaryNumber, int(tmax), int(tmin))
    return detection.detect(primaryName, primaryNumber, secondaryNumber, int(tmax))
    
def stop(data):
    print("here2")
    print(thread.toString())
    stringCmd = 'Command: ' + data[0]
    print(stringCmd)
    
    s.send(stringCmd)
    #detection.stop()
    thread.exit()
    
def client_connects():    
    print('Client Connected!')
    
def client_disconnects():
    print('Client Disconnected!')

s = BluetoothServer(data_received, True, 'hci0', 1, 'utf-8', False, client_connects, client_disconnects)
print(s.client_address)
pause()

