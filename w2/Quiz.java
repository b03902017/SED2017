import java.io.*;
import java.util.*;
import java.lang.*;

class Alarm implements Comparable<Alarm> {
  private int _time;
  private String _alarmMessage;
  Alarm(int time, String deviceName) {
    this._time = time;
    this._alarmMessage = String.format("[%d] %s falls", time, deviceName);
  }
  Alarm(int time, String patientName, String deviceName, Float factor) {
    this._time = time;
    this._alarmMessage = String.format("[%d] %s is in danger! Cause: %s %.1f",
                                       time, patientName, deviceName, factor);
  }
  public String toString() { return this._alarmMessage; }
  public int compareTo(Alarm alarm) { return (this._time - alarm._time); }
}

class Device {
  private String _category;
  private String _name;
  private Float _lowBound;
  private Float _upBound;
  private List<Float> _readFactors;
  // private List<Alarm> _alarms;
  Device(String category, String name, Float lowBound, Float upBound) {
    this._category = category;
    this._name = name;
    this._lowBound = lowBound;
    this._upBound = upBound;
  }
  static boolean isFailed(Float factor) {
    return (factor == -1) ? true : false;
  }
  public boolean isSafe(Float factor) {
    return (factor >= this._lowBound && factor <= this._upBound) ? true : false;
  }
  public String name() { return this._name; }
  public void setReadFactors(List<Float> readFactors) {
    this._readFactors = readFactors;
  }
  public List<Float> readFactors() { return this._readFactors; }
  public String toString() {
    return String.format("%s %s", this._category, this._name);
  }
}

class Patient {
  private String _name;
  private int _period;
  private List<Device> _devices;
  Patient() { ; }
  Patient(String name, int period) {
    this._name = name;
    this._period = period;
    this._devices = new ArrayList<Device>();
  }
  public void addDevice(Device device) { this._devices.add(device); }
  public String name() { return this._name; }
  public int period() { return this._period; }
  public List<Device> devices() { return this._devices; }
}

public class Quiz {
  static int monitorPeriod = 0;
  static List<Patient> patients = new ArrayList<Patient>();
  static List<Alarm> alarms = new ArrayList<Alarm>();

  static List<Float> readFactorDataset(Scanner datasetScanner, Device device,
                                       Patient currentPatient) {
    List<Float> readFactors = new ArrayList<Float>();
    int time = 0;
    while (datasetScanner.hasNextLine()) {
      if (time > monitorPeriod) {
        break;
      }
      Float factor = Float.parseFloat(datasetScanner.nextLine());
      readFactors.add(factor);
      if (Device.isFailed(factor)) {
        alarms.add(new Alarm(time, device.name()));
      } else if (!device.isSafe(factor)) {
        alarms.add(
            new Alarm(time, currentPatient.name(), device.name(), factor));
      }
      time += currentPatient.period();
    }
    while (time <= monitorPeriod) {
      Alarm alarm = new Alarm(time, device.name());
      alarms.add(alarm);
      time += currentPatient.period();
    }
    return readFactors;
  }

  static void readInput(Scanner scanner) {
    Patient currentPatient = new Patient();
    while (scanner.hasNextLine()) {
      String[] line = scanner.nextLine().split(" ");
      if (line.length == 1) {
        monitorPeriod = Integer.parseInt(line[0]);
      } else if (line[0].equals("patient")) {
        String name = line[1];
        int period = Integer.parseInt(line[2]);
        Patient patient = new Patient(name, period);
        patients.add(patient);
        currentPatient = patient;
      } else {
        String category = line[0];
        String name = line[1];
        Float lowBound = Float.parseFloat(line[3]);
        Float upBound = Float.parseFloat(line[4]);
        Device device = new Device(category, name, lowBound, upBound);
        try {
          File dataset = new File(line[2]);
          Scanner datasetScanner = new Scanner(dataset);
          List<Float> readFactors =
              readFactorDataset(datasetScanner, device, currentPatient);
          device.setReadFactors(readFactors);
          datasetScanner.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
        currentPatient.addDevice(device);
      }
    }
  }

  static void dumpAlarms() {
    Collections.sort(alarms);
    for (Alarm alarm : alarms) {
      System.out.println(alarm);
    }
  }

  static void dumpDataBase() {
    for (Patient patient : patients) {
      System.out.printf("patient %s\n", patient.name());
      for (Device device : patient.devices()) {
        System.out.println(device);
        List<Float> readFactors = device.readFactors();
        int time = 0;
        for (Float factor : readFactors) {
          if (time > monitorPeriod) {
            break;
          }
          System.out.printf("[%d] %.1f\n", time, factor);
          time += patient.period();
        }
        while (time <= monitorPeriod) {
          System.out.printf("[%d] -1.0\n", time);
          time += patient.period();
        }
      }
    }
  }
  public static void main(String[] argv) {
    try {
      File inputFile = new File(argv[0]);
      Scanner scanner = new Scanner(inputFile);
      readInput(scanner);
      scanner.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    dumpAlarms();
    dumpDataBase();
  }
}
