# Download the helper libra from httos://www,twilio.com/docs.python/install
from twilio.rest import Client

class notifications:
    
    def __init__(self,pName,pNum,sNum):
        self.primary_number = '+1' + pNum
        self.primary_name = pName
        self.secondary_number = '+1' + sNum
        
    def set_caregiver_info(self,pName,pNum,sNum):
        self.primary_number = '+1' + pNum
        self.primary_name = pName
        self.secondary_number = '+1' + sNum

    #notify primary caregiver
    def notify_primary_caregiver(self):

        # Account Sid and Auth Token from wilion
        account_sid = 'AC6fd284f16975e1409e19e513b6de057f'
        auth_token = 'f5c7fdfa0f179eb064d9874a0e56abcd'
        client = Client(account_sid, auth_token)

        message = client.messages \
                        .create(
                        body="Please check your car! You may have left your child in the backseat.",
                        from_='+18064969155',
                        to=self.primary_number
                        )

        # print(message.sid)

    #notify secondary caregiver
    def notify_secondary_caregiver(self):

        # Account Sid and Auth Token from twilion
        account_sid = 'AC6fd284f16975e1409e19e513b6de057f'
        auth_token = 'f5c7fdfa0f179eb064d9874a0e56abcd'
        client = Client(account_sid, auth_token)

        message = client.messages \
                        .create(
                        body="There has been a child detected in " + self.primary_name + "'s car. Please take further action.\n\nIf child is not removed from car within 5 minutes emergency services will be contacted.",
                        from_='+18064969155',
                        to=self.secondary_number
                        )

        #print(message.sid)

    #notify 911
    #def notify_emergency_personal():

