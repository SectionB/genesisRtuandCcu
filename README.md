


**~~MIXTURE PLANT CONTROL~~**






**AIM**

To Design and implement Control System software to operate

the manufacture plant.

**System Overview**

**Operational Rules of the System**

• The valve V1 should be in OPEN and the valve V2 should be

in CLOSE position before the pump P1 is started.

• The valve V2 should be in OPEN and the valve V1 should be

in CLOSE position before the pump P2 is started.

• The Valves V1, V2 and V3 should be in CLOSE position

before the motor (mixer or agitator) is started.





• The motor M1 and M2 should be ON for exact duration (N

seconds)

• The valve V3 can be opened only after the duration (R

seconds) after the motor M1 and M2 are stopped.

• The valve V1 and V2 should be in CLOSE position before the

valve V3 is OPEN.

**PROCEDURE**

The user will be provided a GUI for operating the CCS to control

and monitor the RTU. A separate GUI is provided for the user to

see the current status of the RTU. A byte array is used to

communicate between the CCS and RTU via a TCP socket

according to Genisys Protocol.

**Genisys Protocol**

The Genisys protocol is protocol that is binary, byte oriented,

polling enabled protocol where each message has a unique

header/control and terminator bytes.





**Typical Genisys Protocol Message Format**

**Basic Genisys Protocol Master to Slave Messages (CCS to RTU)**

• Acknowledge and Poll Message

• Poll Message

• Control Data Message

• Indication Recall (Master Recall) Message

• Control Execute Message

**Basic Genisys Protocol Slave to Master Messages (RTU to CCS)**

• Acknowledge Message

• Indication Data Message

• Control Checkback Message





**CCS (Control Center System)**

Here an TCP port is instantiated for the communication. For

implementing the Genisys protocol separate byte variables are

initialized for each of the 5 parts of the Genisys Protocol Message

Blocks. That is a control header byte, a station address byte, a

data byte, a security checksum byte and a terminator character

byte.

In addition, bytes variables are assigned for the server(master-

CCS) messages that are sent to the client(slave-RTU) and for the

client(slave-RTU) messages that received by the server(master-

CCS).

Separate methods are written for handling the above types of

messages received by the server and also for the messages that

are sent by the server.

Figure : 1 GUI of the CCS





**RTU (Remote Terminal Unit)**

Same as the CCS, here also an TCP port(should be the same port

used by the server) is used for the communication. All the other

features are almost similar to the operation of the CCS except

here the RTU works as a client/slave. That means implementing

the Genisys protocol and handling the client-server messages.

Figure 2 : GUI of the RTU





**Functional Requirements**

A separate java class is used for adding the functional

requirements to the system. These are conditions that should

be fulfilled for each part of the Mixture Plant system to work.

For each control message that is being sent from the server it

will check whether that particular condition of the system could

be allowed to be active.

**Use of threads**

Separate threads are used for the following functions.

• Polling (for checking the connection between CCS and

RTU)

• Keeping the Motors M1 and M2 on for an exact duration

(N seconds).

• Making it possible to open Valve V3 only after Motors M1

and M2 are turned off for an exact duration (R seconds).





**Example Usage of the System**

Figure 3 : Active Mixture Plant


