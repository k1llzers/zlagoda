import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Card from '@mui/material/Card';
import {
    Box,
    Button, FormControl,
    Dialog, DialogContent,
    IconButton,
    Stack,
    Typography, MenuItem, Grid
} from "@mui/material";
import Collapse from '@mui/material/Collapse';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import DeleteOutlineOutlinedIcon from '@mui/icons-material/DeleteOutlineOutlined';
import {useEffect, useState} from "react";
import axios from "axios";
import {useAuth} from "../provider/authProvider";
import {Container} from "react-bootstrap";
import Alert from "@mui/material/Alert";
import AddIcon from '@mui/icons-material/Add';
import SearchIcon from '@mui/icons-material/Search';
import ModeEditIcon from '@mui/icons-material/ModeEdit';
import StyledTableCell from "../styledComponent/styledTableCell";
import StyledTextField from "../styledComponent/styledTextField"
import StyledButton from "../styledComponent/styldButton";
import StyledTableRow from "../styledComponent/styledTableRow";
import SearchContainer from "../styledComponent/searchContainer";
import SearchIconWrapper from "../styledComponent/searchIconWrapper";
import StyledInputBase from "../styledComponent/styledInputBase";
import StyledLabel from "../styledComponent/styldLabel";
import StyledSelect from "../styledComponent/styledSelect";
import StyledDatePicker from "../styledComponent/styledDatePicker";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider} from "@mui/x-date-pickers";
import dayjs from "dayjs";
import 'dayjs/locale/uk'

const Employee = () => {
    const [employees, setEmployees] = useState([])
    const {role} = useAuth()
    const [errorMessage, setErrorMessage] = useState("");
    const [openForm, setOpenForm] = useState(false)
    const [updateRow, setUpdateRow] = useState(undefined)
    const [search, setSearch] = useState("")
    const [employeeFilter, setEmployeeFilter] = useState(0)

    const fetchEmployeesData = async () => {
        let response
        if (employeeFilter === 0)
            response = await axios.get("http://localhost:8080/api/employee/order-by/surname")
        else
            response = await axios.get("http://localhost:8080/api/employee/cashier/order-by/surname")
        setEmployees(response.data);
    };

    useEffect(() => {
        fetchEmployeesData();
    }, [employeeFilter]);

    const handleDelete = async (employeeId) => {
        const response = await axios.delete("http://localhost:8080/api/employee/" + employeeId)
        if (response.data.error) {
            setErrorMessage("Can't delete employee that present at least one check")
            setTimeout(() => setErrorMessage(""), 3500)
        } else {
            if (response.data === true)
                setEmployees(employees.filter(employee => employee.id !== employeeId));
        }
    }

    function handleUpdate(row) {
        setUpdateRow(row)
        handleOpenForm()
    }

    const fetchSearchingEmployeeData = async (surname) => {
        const response = await axios.get("http://localhost:8080/api/employee/address/phone?surname=" + surname)
        setEmployees(response.data);
    };

    const handleSearchChange = async (e) => {
        setSearch(e.target.value)
        setEmployeeFilter(0)
        if (e.target.value.trim().length > 0)
            await fetchSearchingEmployeeData(e.target.value)
        else
            await fetchEmployeesData()
    };

    function handleOpenForm() {
        setOpenForm(true)
    }

    function handleClose(){
        setOpenForm(false)
        setUpdateRow(undefined)
    }

    const clear = e => {
        setErrorMessage("")
    };

    function processEmployee(employee) {
        return {
            id: employee.id,
            surname: employee.surname,
            name: employee.name,
            patronymic: employee.patronymic,
            salary: employee.salary,
            dateOfBirth: employee.dateOfBirth,
            dateOfStart: employee.dateOfStart,
            phoneNumber: employee.phoneNumber,
            role: employee.role.charAt(0).toUpperCase() + employee.role.toLowerCase().slice(1),
            city: employee.city,
            street: employee.street,
            zipCode: employee.zipCode
        }
    }

    const columns = [
        {field: 'surname', headerName: 'Surname'},
        {field: 'name', headerName: 'Name'},
        {field: 'patronymic', headerName: 'Patronymic'},
        {field: 'phoneNumber', headerName: 'Phone number'},
        {field: 'dateOfStart', headerName: 'Date of start'}
    ];

    const rows = employees.map((employee) => processEmployee(employee));

    function EmployeeForm(props) {
        const minDateOfBirth = dayjs().subtract(18, 'year')
        const {onClose, open, row} = props;
        const [login, setLogin] = useState("")
        const [role, setRole] = useState(row ? row.role : "")
        const [surname, setSurname] = useState(row ? row.surname : "")
        const [name, setName] = useState(row ? row.name : "")
        const [patronymic, setPatronymic] = useState(row ? row.patronymic ? row.patronymic : "" : "")
        const [phoneNumber, setPhoneNumber] = useState(row ? row.phoneNumber : "+380")
        const [salary, setSalary] = useState(row ? row.salary : 1000)
        const [dateOfStart, setDateOfStart] = useState(row ? dayjs(row.dateOfStart) : dayjs('2022-04-17'))
        const [dateOfBirth, setDateOfBirth] = useState(row ? dayjs(row.dateOfBirth) : minDateOfBirth)
        const [city, setCity] = useState(row ? row.city : "")
        const [street, setStreet] = useState(row ? row.street : "")
        const [zipCode, setZipCode] = useState(row ? row.zipCode : "")
        const [disableAdd, setDisableAdd] = useState(true)
        const phoneCheck = /^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{6}$/im;

        useEffect(() => {
            if((login.trim() && login.length || row !== undefined) < 20 && role && surname.trim() && name.trim()
            && phoneNumber.trim() && salary && city.trim() && street.trim() && zipCode.trim()
            && surname.length < 100 && name.length < 100 && patronymic.length < 100
            && salary > 0 && phoneCheck.test(phoneNumber) && phoneNumber.length < 14 && city.length < 50
            && street.length < 50 && zipCode.length < 9 && dateOfBirth < minDateOfBirth
            ) {
                setDisableAdd(false)
            }else {
                setDisableAdd(true)
            }

        }, [login, role, surname, name, patronymic, phoneNumber, salary, dateOfStart, dateOfBirth,
            city, street, zipCode
        ]);

        const handleAdd = async () => {
            handleClose()
            let response;
            if(!row) {
                response = await axios.post("http://localhost:8080/api/employee", {
                    login:login,
                    surname: surname,
                    name: name,
                    patronymic: patronymic ? patronymic : null,
                    salary: +salary,
                    dateOfBirth: dateOfBirth,
                    dateOfStart: dateOfStart,
                    phoneNumber: phoneNumber,
                    role: role.toUpperCase(),
                    city: city,
                    street: street,
                    zipCode: zipCode
                })
            } else {
                response = await axios.put("http://localhost:8080/api/employee", {
                    id: row.id,
                    surname: surname,
                    name: name,
                    patronymic: patronymic ? patronymic : null,
                    salary: +salary,
                    dateOfBirth: dateOfBirth,
                    dateOfStart: dateOfStart,
                    phoneNumber: phoneNumber,
                    role: role.toUpperCase(),
                    city: city,
                    street: street,
                    zipCode: zipCode
                })
            }
            if (response.data.error) {
                setErrorMessage(response.data.error)
                setTimeout(() => setErrorMessage(""), 3500)
            } else {
                fetchEmployeesData()
            }
        }

        return (
            <Dialog onClose={onClose} open={open}>
                <DialogContent>
                    <FormControl fullWidth>
                        {!row && <StyledTextField
                            id="outlined-basic"
                            label="Login"
                            variant="outlined"
                            value={login}
                            required
                            error={login.length > 20}
                            helperText={zipCode.length > 20 ? "Too long" : ""}
                            onChange={(event) => {setLogin(event.target.value)}}/>}
                        <StyledTextField
                            id="outlined-basic"
                            label="Surname"
                            variant="outlined"
                            value={surname}
                            required
                            multiline
                            error={surname.length > 100}
                            helperText={surname.length > 100 ? "Too long" : ""}
                            onChange={(event) => {setSurname(event.target.value)}}/>
                        <StyledTextField
                            id="outlined-basic"
                            label="Name"
                            variant="outlined"
                            value={name}
                            required
                            multiline
                            error={name.length > 100}
                            helperText={name.length > 100 ? "Too long" : ""}
                            onChange={(event) => {setName(event.target.value)}}/>
                        <StyledTextField
                            id="outlined-basic"
                            label="Patronymic"
                            variant="outlined"
                            value={patronymic}
                            multiline
                            error={patronymic.length > 100}
                            helperText={patronymic.length > 100 ? "Too long" : ""}
                            onChange={(event) => {setPatronymic(event.target.value)}}/>
                        <Box sx={{width:"99%", margin: '10px', display:"flex", justifyContent:"space-between"}}>
                            <LocalizationProvider dateAdapter={AdapterDayjs}>
                                <Box sx={{margin:0 ,width:"100%"}}>
                                    <StyledDatePicker
                                        label="Date of birth"
                                        views={['year', 'month', 'day']}
                                        maxDate={minDateOfBirth}
                                        value={dateOfBirth}
                                        onChange={(newValue) => setDateOfBirth(newValue)}
                                    />
                                </Box>
                            </LocalizationProvider>
                            <LocalizationProvider dateAdapter={AdapterDayjs}>
                                <Box sx={{margin:0 ,width:"100%"}}>
                                    <StyledDatePicker
                                        label="Date of start"
                                        views={['year', 'month', 'day']}
                                        value={dateOfStart}
                                        onChange={(newValue) => setDateOfStart(newValue)}
                                    />
                                </Box>
                            </LocalizationProvider>
                        </Box>
                        <Box sx={{ flexGrow: 1 }}>
                            <Grid container>
                                <Grid item xs={6}>
                                    <FormControl variant="outlined" sx={{minWidth:"100%"}}>
                                        <StyledLabel variant="outlined" id="demo-simple-select-label" required>
                                            Role
                                        </StyledLabel>
                                        <StyledSelect
                                            labelId="demo-simple-select-label"
                                            id="demo-simple-select"
                                            label="Role"
                                            value={role}
                                            onChange={(event) => {setRole(event.target.value)}}
                                        >
                                            <MenuItem
                                                key={"Manager"}
                                                value={"Manager"}
                                            >{"Manager"}</MenuItem>
                                            <MenuItem
                                                key={"Cashier"}
                                                value={"Cashier"}
                                            >{"Cashier"}</MenuItem>
                                        </StyledSelect>
                                    </FormControl>
                                </Grid>
                                <Grid item xs={6}>
                                    <StyledTextField
                                        sx={{minWidth:"93%"}}
                                        id="outlined-basic"
                                        label="Salary"
                                        variant="outlined"
                                        value={salary}
                                        required
                                        multiline
                                        error={salary < 1}
                                        helperText={salary < 1 ? "Less than 1" : ""}
                                        onChange={(event) => {setSalary(event.target.value)}}/>
                                </Grid>
                                <Grid item xs={12}>
                                    <StyledTextField
                                        sx={{width:"97%"}}
                                        id="outlined-basic"
                                        label="Phone number"
                                        variant="outlined"
                                        value={phoneNumber}
                                        required
                                        multiline
                                        error={!phoneCheck.test(phoneNumber)}
                                        helperText={!phoneCheck.test(phoneNumber) ? "Incorrect" : ""}
                                        onChange={(event) => {setPhoneNumber(event.target.value)}}/>
                                </Grid>
                                <Grid item xs={12}>
                                    <StyledTextField
                                        sx={{width:"97%"}}
                                        id="outlined-basic"
                                        label="Street"
                                        variant="outlined"
                                        value={street}
                                        required
                                        multiline
                                        error={street.length > 50}
                                        helperText={street.length > 50 ? "Too long" : ""}
                                        onChange={(event) => {setStreet(event.target.value)}}/>
                                </Grid>
                                <Grid item xs={6}>
                                    <StyledTextField
                                        sx={{width:"94%"}}
                                        id="outlined-basic"
                                        label="City"
                                        variant="outlined"
                                        value={city}
                                        required
                                        multiline
                                        error={city.length > 50}
                                        helperText={city.length > 50 ? "Too long" : ""}
                                        onChange={(event) => {setCity(event.target.value)}}/>
                                </Grid>
                                <Grid item xs={6}>
                                    <StyledTextField
                                        sx={{width:"94%"}}
                                        id="outlined-basic"
                                        label="Zip code"
                                        variant="outlined"
                                        value={zipCode}
                                        required
                                        multiline
                                        error={zipCode.length > 9}
                                        helperText={zipCode.length > 9 ? "Too long" : ""}
                                        onChange={(event) => {setZipCode(event.target.value)}}/>
                                </Grid>
                            </Grid>
                        </Box>
                        <StyledButton
                            variant="text"
                            sx={{width: '50%', alignSelf: 'center'}}
                            onClick={handleAdd}
                            disabled={disableAdd}
                        >{row ? "Update" : "Add"}</StyledButton>
                    </FormControl>
                </DialogContent>
            </Dialog>
        )
    }


    function Row(props) {
        const {row} = props;
        const [open, setOpen] = useState(false);

        return (
            <React.Fragment>
                <TableRow sx={{'& > *': {borderBottom: 'unset'}}}>
                    <StyledTableCell>
                        <IconButton
                            aria-label="expand row"
                            size="small"
                            onClick={() => setOpen(!open)}>
                            {open ? <KeyboardArrowUpIcon/> : <KeyboardArrowDownIcon/>}
                        </IconButton>
                    </StyledTableCell>
                    <StyledTableCell component="th" scope="row " align="center">{row.surname}</StyledTableCell>
                    <StyledTableCell component="th" scope="row " align="center">{row.name}</StyledTableCell>
                    <StyledTableCell component="th" scope="row " align="center">{row.patronymic}</StyledTableCell>
                    <StyledTableCell component="th" scope="row " align="center">{row.phoneNumber}</StyledTableCell>
                    {!search && <StyledTableCell component="th" scope="row " align="center">{row.dateOfStart}</StyledTableCell>}
                    {role === "MANAGER" && <StyledTableCell align="right">
                        <Button onClick={() => handleUpdate(row)}>
                            <ModeEditIcon color='action'/>
                        </Button>
                        <Button onClick={() => handleDelete(row.id)}>
                            <DeleteOutlineOutlinedIcon color="error"/>
                        </Button>
                    </StyledTableCell>}
                </TableRow>
                <TableRow>
                    <StyledTableCell style={{paddingBottom: 0, paddingTop: 0}} colSpan={7}>
                        <Collapse in={open} timeout="auto" unmountOnExit>
                            <Box sx={{margin: 1}}>
                                <Typography variant="span" gutterBottom component="div" style={{fontSize: 19}}>
                                    Details
                                </Typography>
                                {
                                    !search ?
                                        <p>Role: {row.role} <br/>
                                            Salary: {row.salary} ₴ <br/>
                                            Date of birth: {row.dateOfBirth} <br/>
                                            Address: {row.city}, {row.street} street, {row.zipCode}</p>
                                        :
                                        <p>Address: {row.city}, {row.street} street, {row.zipCode}</p>
                                }
                            </Box>
                        </Collapse>
                    </StyledTableCell>
                </TableRow>
            </React.Fragment>
        );
    }

    function EmployeeTable() {
        let width = search ? "18.7%" : "15%"

        if (search)
            columns.pop()

        return (
            <React.Fragment>
                <TableContainer component={Card} sx={{ maxWidth: 1000, margin: '30px auto' }}>
                    <Table aria-label="collapsible table">
                        <TableHead>
                            <StyledTableRow>
                                <StyledTableCell/>
                                {columns.map((column) => (
                                    <StyledTableCell align="center" key={column.field} sx={{width: {width}}}>
                                        {column.headerName}
                                    </StyledTableCell>
                                ))}
                                <StyledTableCell sx={{width:"20%"}} />
                            </StyledTableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row) => (
                                <Row key={row.id} row={row} />
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </React.Fragment>
        )
    }

    return (
        <Container style={{ marginTop: '50px'}}>
            <EmployeeForm
                open={openForm}
                onClose={handleClose}
                row={updateRow}
            />
            <Box sx={{maxWidth: 1000, margin: '0 auto'}}>
                <Stack direction='row' justifyContent='space-between'>
                    <StyledButton variant="outlined"
                                  startIcon={<AddIcon />}
                                  onClick={handleOpenForm}
                                  sx={{maxHeight:'40px', marginTop:'10px'}}
                    >
                        Add
                    </StyledButton>
                    <FormControl variant="outlined" size="small" sx={{maxHeight:'40px', minWidth:'120px'}}>
                        <StyledLabel variant="outlined" id="demo-simple-select-label">
                            Employees
                        </StyledLabel>
                        <StyledSelect sx={{maxHeight:'40px', minWidth:'120px'}}
                            labelId="demo-simple-select-label"
                            id="demo-simple-select"
                            label="Employees"
                            value={employeeFilter}
                            onChange={(event) => {setEmployeeFilter(event.target.value); setSearch("")}}
                        >
                            <MenuItem
                                key={"All"}
                                value={0}
                            >All</MenuItem>
                            <MenuItem
                                key={"Cashier"}
                                value={1}
                            >Cashier</MenuItem>
                        </StyledSelect>
                    </FormControl>
                    <SearchContainer sx={{maxHeight:'40px', marginTop:'10px'}}>
                        <SearchIconWrapper>
                            <SearchIcon />
                        </SearchIconWrapper>
                        <StyledInputBase
                            placeholder="Search…"
                            value={search}
                            inputProps={{ 'aria-label': 'search' }}
                            onChange={handleSearchChange}
                        />
                    </SearchContainer>
                </Stack>
                {errorMessage && <Alert style={{width: '50%', fontSize: '15px', position: 'fixed', right: '25%', top: '5%'}} severity="error" onClose={clear}>{errorMessage}</Alert>}
                <EmployeeTable/>
            </Box>
        </Container>
    );
}

export default Employee;