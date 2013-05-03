Spring13-beatmaking
===================



Our beat making app enables users to create Electronic Music on their Android devices.  The user can compose a Track made up of up to 4 Patterns using the “Tracks” screen which allows the user to add/remove patterns to/from the Track.
The Patterns can be recorded using the Drum Machine. 
A Drum Machine is a 4x4 Grid of cells (Pads) each of which represents a Sound Sample. The Drum machine has a default set of assigned sounds and new sounds can be assigned to the pads via the edit drum machine screen. During a pattern's playback, a metronome sound will play to help the user keep track of the beats per minute. The pattern will continue looping until the stop button is pressed. During playback the user may play beats by touching one of the 16 pads on the screen. The beat gets recorded and added to the pattern only if the record button is pressed. The recorded beat will then be included in the playback. Pads will be lit up as the corresponding sounds are played during playback.

Video Preview at:  http://www.youtube.com/watch?v=hp-fwyZaVDM

                                                                     
  
  
  BEATMAKING APP TECHNICAL DOCUMENTATION                        
                                                                     

*********************************************************************************
PatternActivity.java

This class handles the pattern activity, which contains a 4x4 grid of buttons that have sounds assigned to them.  The java code contains more-in-depth comments. The class contains functionality to play, stop, and record sounds.  The class also allows editing aspects of the current pattern, including tempo (bpm) and number of bars.  The class also allows the current pattern to be cleared and exported into a wav file, and allows the toggling on and off of the metronome.  Finally, the class allows transitioning to the Track Activity and Edit Drum Machine Activity.

When the activity is created, the button states are set to the default values as stored by the shared preferences.  Then, the current phone ringer state is tested.  Next, the various elements of the activity are updated to reflect the current pattern. The playback thread is initialized to handle sound playback and the looping of the pattern when the play state is true.  The play and record button functionalities are implemented to start and stop the playback.  Also, the pattern options button is implemented, as well as the buttons to transition to the Track Activity and Edit Drum Machine Activity



                                             
*****************************************************************************************
Edit Drim Machine :

This activity is launched when the Edit Drum Machine button is touched in the PatternActivity.

This activity provides 2 important functionalities. It enables the user to change the sound sample assigned to the drum pads and also lets the user rename the pads as per the convinience of the user.

The user may touch the drum pads to launch a set of application options. These are the applications that can access the only the music files on the device. This is done by the setOnClickListener method where an OnClickListener is registered on the buttons. The user can then choose an application to select a music file. Once the file is chosen the control is returned back to this application. The control comes back to this activity into the onActivityResult method. Here the file path is stored for future uses of this application. For example if the user exits the app and re launches it then the pads should play the same sounds that were ssigned to them the lasttime around. The onActivityResult method also loads this sound into the soundpool and actually assigns it to the pad that was clicked. Lastly it sets the name ofthe pad to the name of the sound file.

The user may touch the drum pads for a longer duration (a long click) to launch a dialog for renaming the button. This takes the control to the RenameDialogFragment DialogFragment. Here the user is shown a dialog for renaming the drum pad. On renaming the drum pad the user taps the 'Rename' button which returns the control to this activity. Here the control enters onDialogPositiveClick method. Here the method sets the text on the drum pad and stores it (persists it) in a shared prefence for future use.                                                                     
                                                                                                                                   
                                            
************************************************************

WaveIO.java

This class handles the exporting of sounds to a wav file. The main functions of this class are
1.) public boolean exportSound(String fileName,PriorityQueue<Sound> priorityQueue, Context ctxt) : Principle function used to export the priority queue of sounds as a wav file. Called from the layer above.

2.) public byte[] read(Context ctxt, String fileName): returns a byte array of the contents of the file which we have read.


3.) boolean writeWaveFileHeaders(Context ctxt, String fileName,long totalChunkSize): Write the wave file headers to the newly created .wav file.
call this function after calculating the total file size to ensure the correct writing of headers.

4.) public boolean save(Context ctxt, String fileName, byte[] myData): append the output to the wav file 


For more detailed wave file specs, refer to in-code documentation.

************************************************************************************
