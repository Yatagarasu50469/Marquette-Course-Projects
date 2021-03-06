# 
# Synthesis run script generated by Vivado
# 

set_msg_config -id {Common 17-41} -limit 10000000
set_msg_config -id {HDL 9-1061} -limit 100000
set_msg_config -id {HDL 9-1654} -limit 100000
set_msg_config -id {Synth 8-256} -limit 10000
set_msg_config -id {Synth 8-638} -limit 10000
create_project -in_memory -part xc7a35tcpg236-1

set_param project.compositeFile.enableAutoGeneration 0
set_param synth.vivado.isSynthRun true
set_property webtalk.parent_dir C:/Users/Yatagarasu/control_unit/control_unit.cache/wt [current_project]
set_property parent.project_path C:/Users/Yatagarasu/control_unit/control_unit.xpr [current_project]
set_property default_lib xil_defaultlib [current_project]
set_property target_language VHDL [current_project]
set_property vhdl_version vhdl_2k [current_fileset]
read_vhdl -library xil_defaultlib {
  C:/Users/Yatagarasu/control_unit/control_unit.srcs/sources_1/imports/Control_unit/Control_Unit.vhd
  C:/Users/Yatagarasu/control_unit/control_unit.srcs/sources_1/imports/Control_unit/Control_Unit_tb.vhd
}
synth_design -top Control_Unit_tb -part xc7a35tcpg236-1
write_checkpoint -noxdef Control_Unit_tb.dcp
catch { report_utilization -file Control_Unit_tb_utilization_synth.rpt -pb Control_Unit_tb_utilization_synth.pb }
