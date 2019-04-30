import time
import busio
import board
import adafruit_amg88xx
import numpy as np
import notifications

isStopped = False
alert_log = []

def stop():
    isStopped = True

##def detect( pName, pNum, sNum, tmax, tmin ):
def detect( pName, pNum, sNum, delta ):
    alert_log = []
    isStopped = False
    n = notifications.notifications( pName, pNum, sNum )
    
    alert = {   1 : { "timeout" : 15, "message" : "Alerting " + "1st Caregiver" },
                2 : { "timeout" : 15, "message" : "Alerting " + "1st Caregiver (Again)" },
                3 : { "timeout" : 15, "message" : "Alerting " + "2nd Caregiver" },
                911 : { "timeout" : 0, "message" : "Alerting 911", "phone" : 911 } }

    for t in alert:
        if isStopped:
            print("stopped")
            break
        
        ## if isChildDetected(tmax,tmin):
        if isChildDetectedDelta(delta):
            print( alert[t]["message"] )
            # send alert
            if t is not 3 and t is not 911:
                n.notify_primary_caregiver()
                caregiver_name = "1st"
            else:
                n.notify_secondary_caregiver()
                caregiver_name = "2nd"
            alert_log.append(caregiver_name + " Caregiver: " + time.asctime())
            sleep( alert[t]["timeout"] )
        else:
            break
    
    print("DONE") 
    return alert_log
    
def sleep( t ):
    for i in range(t):
        if not isStopped:
            time.sleep(1)
        else:
            print("stopped")
            break


def isChildDetectedRange(tmax,tmin):
    matrix = getAvgTemperature( 5 )
    for row in matrix:
        for temp in row:
            if temp >= tmin and temp <= tmax:
                return True

    return False

def isChildDetectedDelta( delta ):
    matrixDelta = getDeltaAvgTemperature( 10, 2, 5, 3, 4 )
    
    if matrixDelta >= delta:
        return True
    else:
        return False


def getTemperature():
    #matrix_test = [  [30,30,30,30,30,30,30,30],
    #            [30,30,30,30,30,30,30,30],
    #            [30,30,30,30,30,30,30,30],
    #            [30,30,30,30,30,30,30,30],
    #            [30,30,30,30,37,30,30,30],
    #            [30,30,30,30,30,30,30,30],
    #            [30,30,30,30,30,30,30,30],
    #            [30,30,30,30,30,30,30,30]]
    
    i2c = busio.I2C(board.SCL, board.SDA)
    amg = adafruit_amg88xx.AMG88XX(i2c)

    time.sleep(3)
    matrix = amg.pixels
    # print( matrix )

    return matrix

# s = number readings per second to compute
def getAvgTemperature( s ):
 
    i2c = busio.I2C(board.SCL, board.SDA)
    amg = adafruit_amg88xx.AMG88XX(i2c)

    total = [ [0]*8 for k in range(8) ]
    for t in range(s):
        time.sleep(0.1)
        matrix = amg.pixels
        for i in range( len(matrix) ):
            for j in range( len(matrix[0]) ):
                total[i][j]+=matrix[i][j]
    
    for i in range( len(matrix) ):
        for j in range( len(matrix[0]) ):
            total[i][j] = total[i][j]/s
            
    # print( matrix )
    return total

def getDeltaAvgTemperature( s, r1, r2, c1, c2 ):
 
    i2c = busio.I2C(board.SCL, board.SDA)
    amg = adafruit_amg88xx.AMG88XX(i2c)
    
    total = [ [0]*8 for k in range(8) ]
    for t in range(s):
        time.sleep(0.1)
        matrix = amg.pixels
        for i in range( len(matrix) ):
            for j in range( len(matrix[0]) ):
                if i >= r1 and i <= r2 and j >= c1 and j <= c2:
                    total[i][j]+=matrix[i][j]
                elif i == 0 or i == len(matrix[0]) or j == 0 or j == len(matrix[0]):
                    total[i][j]+=matrix[i][j]
    
    inside = []
    outside = []
    for i in range( len(matrix) ):
        for j in range( len(matrix[0]) ):
            if i >= r1 and i <= r2 and j >= c1 and j <= c2:
                inside.append( total[i][j]/s )
            elif i == 0 or i == len(matrix[0]) or j == 0 or j == len(matrix[0]):
                outside.append( total[i][j]/s )

    maxIn = max(inside)
    maxOut = max(outside)
    minIn = min(inside)
    minOut = min(outside)
    maxTemp = max([maxIn,maxOut])
    minTemp = min([minIn,maxOut])
    
    return abs( maxTemp - minTemp )

##alerts = detect('Madison','8068289661','9152273680', 4)
##print(alerts)

