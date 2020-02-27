import os, pygame, time, random, picamera, picamera.array, cv2, numpy as np, sys, select, serial

print"Accessing Drivers"
class pyscope: #Define a new class
    screen = None #Initialize screen object

    def __init__(self): #Constructor for object
        disp_no = os.getenv("DISPLAY") #Request display id from os
        drivers = ['fbcon','directfb','svgalib'] #List of common drivers
        found = False #Has the display driver been found
        
        for driver in drivers: #For each of the possible driver types
            if not os.getenv('SDL_VIDEODRIVER'): #If driver is not defined in the os
                os.putenv('SDL_VIDEODRIVER', driver) #Add driver as env. variable
            try: #Try
                pygame.display.init() #To initialize the display driver
            except pygame.error: #Unless an error is generated
                print"Driver: {0} failed.".format(driver) #Indicate which failed
                continue
            found = True #Indicate that the driver was found
            break

        if not found: #If a driver was not found
            raise Exception('No suitable video driver found.') #Raise an error
        
        size = (pygame.display.Info().current_w, pygame.display.Info().current_h) #Determine display size
        self.screen = pygame.display.set_mode(size, pygame.FULLSCREEN) #Set display size
        self.screen.fill((0, 0, 0)) #Clear the screen
        pygame.font.init() #Initialize pygame fonts
        pygame.display.update() #Update the display

print"Display Class Established"
scope = pyscope() #Create a display object
print"Display connected"
displaySize = (pygame.display.Info().current_w, pygame.display.Info().current_h) #Obtain display size
print"Opening Serial Port to Arduino"
ser = serial.Serial('/dev/ttyACM0',9600)
print"Serial Port Opened"

#Establish variables
motionCapture = True #Should the motion capture procedure be enabled
found = False
motionCompleted = True #Has the last motion been completed
firstImage = False #Has the first image been captured yet
kernel = np.ones((20,20),np.uint8) #Set minimum filter size
motorXPos = 0 #Set the current x-axis position
motorYPos = 0 #Set the current y-axis position
centerX = 0 #Set the desired x position
centerY = 0 #Set the desired y position
xMove = 0 #How much x-axis movement should be performed
yMove = 0 #How much y-axis movement should be performed
maxX = 200 #Set the upper x coordinate; right
maxY = 300 #Set the upper y coordinate; down
minX = -1000 #Set the minimum x coordinate; left
minY = -800 #Set the minimum y coordinate; up
xMotion = False #Flag if there is already x-axis motor motion
yMotion = False #Flag if there is already y-axis motor motion
motWaitTime = 0.04
#motWaitTime = 0.0069 #How much time to wait after sending motor command (ms)
motorRatio = 10 #How many pixels does a turn command move
xMotorRange = motorRatio
yMotorRange = motorRatio
overlay = None #Initialize empty object to hold rectangular overlay
image = None #Initialize empty object to hold image 

print"Opening Camera Object"

with picamera.PiCamera(framerate=30) as camera: #Open a camera opbject using the pi camera
    camera.hflip = True
    camera.awb_mode = 'auto'
    camera.video_stabilization = False #Turn off video stabilization
    time.sleep(3) #Wait for camera to start
    awb_levels = camera.awb_gains #Get the white balance levels
    camera.awb_mode = 'off' #Turn off auto white balance
    camera.awb_gains = awb_levels #Set the white balance as those initially found
    time.sleep(1) #Wait for camera

    print"Starting the Tracking Loop"
    while True: #While the program is running; True until keypress: 'enter'
     with picamera.array.PiRGBArray(camera) as stream: #Take RGB arrays from camera stream
        if not motionCompleted: #If motion has not yet been completed
          if (-xMotorRange / 2) <= xMove <= (xMotorRange / 2): #If the remaining x-axis motion is within the allowable range
              #print"x-axis motor in range"
              xMotion = False #Indicate that x-axis motion is stopped
          elif (xMove <= 0) and ((motorXPos - motorRatio) > minX): #If the movement is to the left and it is allowable
              #print"Moving x-axis motor clockwise; left"
              ser.write('3') #Move x-axis motor clockwise
              time.sleep(motWaitTime)
              xMotion = True #Indicate there is x-axis motion
              motorXPos = motorXPos - motorRatio #Indicate the change in stored variable
              xMove = xMove + motorRatio #Decrease the remaining amount to move
              print"Remaining xMove: ", xMove
          elif (xMove > 0) and ((motorXPos + motorRatio) < maxX): #If the movement is to the right and it is allowable
              #print"Moving x-axis motor counterclockwise; right"
              ser.write('2') #Move x-axis motor counterclockwise
              time.sleep(motWaitTime)
              xMotion = True #Indicate there is x-axis motion
              motorXPos = motorXPos + motorRatio #Indicate the change in stored variable
              xMove = xMove - motorRatio #Decrease the remaining amount to move
              #print"Remaining xMove:", xMove
          else: #If x-axis motor boundaries
              #print"x-axis boundary"
              xMotion = False

          if (-yMotorRange / 2) <= yMove <= (yMotorRange / 2): #if the remaining y-axis motion is within the allowable range
              print"y-axis motor in range"
              yMotion = False #Indicate that y-axis motion is stopped
          elif (yMove <= 0) and ((motorYPos - motorRatio) > minY): #If the movement is up and it is allowable
              print"Moving y-axis motor clockwise; up"
              ser.write('6') #Move y-axis motor clockwise
              time.sleep(motWaitTime)
              yMotion = True #Indicate there is y-axis motion
              motorYPos = motorYPos - motorRatio #Indicate the change in stored variable
              yMove = yMove + motorRatio #Decrease the remaining amount to move
              print"Remaining yMove: ", yMove
          elif (yMove > 0) and ((motorYPos + motorRatio) < maxY): #If the movement is down and it is allowable
              print"Moving y-axis motor counterclockwise; down"
              ser.write('5') #Move y-axis motor counterclockwise
              time.sleep(motWaitTime)
              yMotion = True #Indicate that there is y-axis motion
              motorYPos = motorYPos + motorRatio #Indicate the change in stored variable
              yMove = yMove - motorRatio #Decrease the remaining amount to move
              print"Remaining yMove: ", yMove
          else: #If y-axis motor boundaries
              print"y-axis boundary"
              yMotion = False #Indicate there is no y-axis motion

          if not yMotion and not xMotion:
             print"Motion completed"
             motionCompleted = True #Indicate that the last motion is completed
             firstImage = False #Reset flag in order to take a new initial image
        if motionCompleted and not motionCapture: #If capture has ended and motion is completed
            print"Program exiting"
            ser.write('1') #Make sure the x-axis movement has stopped
            time.sleep(motWaitTime)
            ser.write('4') #Make sure the y-axis movement has stopped
            time.sleep(motWaitTime)
            break #Exit the while loop and end the program
        if motionCompleted and motionCapture:
         if firstImage: #if the first image has already been taken
             lastGrayImage = grayImage #Set the last image as the one taken in the previous loop run
         camera.capture(stream, format='rgb', use_video_port = True) #Format camera stream as bgr
         image = stream.array #Save the image for processing
         #cv2.imwrite('unfiltered.png',image)
         grayImage = cv2.cvtColor(image, cv2.COLOR_RGB2GRAY) #Change image to grayscale
         if not firstImage: #If this is the first time an image has been captured
             imageSize = image.shape #Get the size of the captured image
             xp = (displaySize[1] - imageSize[1]) / 2 #Establish render x center
             yp = (displaySize[0] - imageSize[0]) / 2 #Establish render y center
         if firstImage:
             imdiff = lastGrayImage - grayImage #Find the difference between the two images
             #cv2.imwrite('rawdiff.png',imdiff)
             ret, imdiff = cv2.threshold(imdiff,127,255,cv2.THRESH_BINARY) #Change to binary image
             imdiff = cv2.erode(imdiff, kernel, iterations = 1) #Filter areas based on kernel size
             #cv2.imwrite('filtered_difference.png',imdiff)
             _, contours, hierarchy = cv2.findContours(imdiff, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE) #Identify areas
             areas = [cv2.contourArea(c) for c in contours] #Save the areas into an object
             if (len(areas)): #For each of the objects
                 cnt = contours[np.argmax(areas)] #Get index of the largest area
                 x, y, w, h = cv2.boundingRect(cnt) #Find parameters for the largest area
                 cv2.drawContours(image, contours, -1, (0, 255, 0), 2) #Draw all areas in green into the color image
                 cv2.rectangle(image, (x, y), (x+w, y+h), (255, 0, 0), 2) #Add red rectangle to largest area to color image
                 centerX = (x + w) / 2 #Calculate the center x position of the largest area
                 centerY = (y + h) / 2 #Calculate the center y position of the largest area
                 xMove = centerX - 320 #Determine amount needed to reach x position
                 yMove = centerY - 240 #Determine amount needed to reach y position
                 print"Movement detected at centerX",centerX,"centerY",centerY
                 print"xMove: ", xMove, " yMove: ", yMove
                 motionCompleted = False #Indicate that a move should be made
             else:
                 print"No movement detected"
         surf = pygame.surfarray.make_surface(np.flipud(np.rot90(image))) #Change image to surface, rotated for landscape
         scope.screen.blit(surf, (0,0)) #Push the surface into the image buffer
         pygame.display.update() #Update the screen with buffer information
         if sys.stdin in select.select([sys.stdin], [], [], 0)[0]: #If the enter key is hit
             ser.write('1') #Stop x-axis motor movement
             ser.write('4') #Stop y-axis motor movement
             print"Resetting camera position"
             motionCapture = False #Indicate that the motion capture procedure should end
             motionCompleted = False #Indicate that the motion has not yet been completed
             xMotion = False
             yMotion = False
             xMove = -motorXPos
             yMove = -motorYPos
         firstImage = True #indicate that the first image has been captured