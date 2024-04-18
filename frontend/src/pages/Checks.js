import {useEffect, useState} from "react";
import {useAuth} from "../provider/authProvider";
import axios from "axios";
import * as React from "react";
import TableContainer from "@mui/material/TableContainer";
import Card from "@mui/material/Card";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import StyledTableRow from "../styledComponent/styledTableRow";
import StyledTableCell from "../styledComponent/styledTableCell";
import TableBody from "@mui/material/TableBody";
import TableRow from "@mui/material/TableRow";
import {Box, Button, IconButton, Stack, Typography} from "@mui/material";
import ReceiptLongIcon from '@mui/icons-material/ReceiptLong';
import {Container} from "react-bootstrap";
import Alert from "@mui/material/Alert";
import dayjs from "dayjs";


const Checks = () => {
    const [checks, setChecks] = useState([])
    const {role} = useAuth()
    const [errorMessage, setErrorMessage] = useState("")
    const [openForm, setOpenForm] = useState(false)
    const [search, setSearch] = useState("")
    const [cashier, setCashier] = useState(0)
    const [dateFrom, setDateFrom] = useState(dayjs(new Date('2023-03-19T01:52:26.000')))
    const [dateTo, setDateTo] = useState(dayjs(new Date('2025-03-19T01:52:26.000')))


    useEffect(() => {
        fetchChecksData()
    }, [])

    const fetchChecksData = async () => {
        let response
        response = await axios.get("http://localhost:8080/api/check/by-employee?from=" + dateFrom.format('YYYY-MM-DDTHH:mm:ss.SSS00') + "&to=" + dateTo.format('YYYY-MM-DDTHH:mm:ss.SSS00'))
        setChecks(response.data)
    }

    const fetchSearchingCheckData = async (id) => {
        const response = await axios.get("http://localhost:8080/api/check/" + id)
        if(response.data.error)
            setChecks([])
        else
            setChecks([response.data])
    }

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

    function processCards(card) {
        return {
            id: card.id,
            surname: card.surname,
            name: card.name,
            patronymic: card.patronymic,
            phoneNumber: card.phoneNumber,
            city: card.city,
            street: card.street,
            zipCode: card.zipCode,
            percent: card.percent
        }
    }

    function processChecks(check) {
        return {
            id: check.id,
            printDate: check.printDate,
            sumTotal: check.sumTotal,
            vat: check.vat,
            employee: processEmployee(check.employee),
            customerCard: processCards(check.customerCard)
        }
    }

    const columns = [
        {field: 'id', headerName: 'Check Number'},
        {field: 'printDate', headerName: 'Print Date'},
        {field: 'sumTotal', headerName: 'Sum Total'},
    ]

    const rows = checks.map((check) => processChecks(check))

    const clear = e => {
        setErrorMessage("")
    };

    function handleOpenForm() {
        setOpenForm(true)
    }

    function handleClose(){
        setOpenForm(false)
    }

    const handleSearch = async (e) => {
        setSearch(e.target.value)
        if (e.target.value.trim().length > 0 && !isNaN(e.target.value))
            await fetchSearchingCheckData(e.target.value)
        else
            await fetchChecksData()
    }

    function handleInfo(row) {

    }

    function Row(props) {
        const {row} = props;
        return (
            <React.Fragment>
                <TableRow sx={{'& > *': {borderBottom: 'unset'}}}>
                    <StyledTableCell component="th" scope="row " align="center">{row.id}</StyledTableCell>
                    <StyledTableCell component="th" scope="row " align="center">{new Date(row.printDate).toLocaleString()}</StyledTableCell>
                    <StyledTableCell component="th" scope="row " align="center">{row.sumTotal}</StyledTableCell>
                    <StyledTableCell align="right">
                        <Button onClick={() => handleInfo(row)}>
                            <ReceiptLongIcon color="action"/>
                        </Button>
                    </StyledTableCell>
                </TableRow>
            </React.Fragment>
        );
    }

    function ChecksTable() {
        return (
            <React.Fragment>
                <TableContainer component={Card} sx={{ maxWidth: 700, margin: '30px auto' }}>
                    <Table>
                        <TableHead>
                            <StyledTableRow>
                                {columns.map((column) => (
                                    <StyledTableCell align="center" key={column.field}>
                                        {column.headerName}
                                    </StyledTableCell>
                                ))}
                            <StyledTableCell/>
                            </StyledTableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row) => (
                                <Row key={row.name} row={row} />
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </React.Fragment>
        )
    }

    return (
        <Container style={{ marginTop: '50px'}}>
            <Box sx={{maxWidth: 900, margin: '0 auto'}}>
                {errorMessage && <Alert style={{width: '40%', fontSize: '15px', position: 'fixed', right: '30%', top: '5%'}} severity="error" onClose={clear}>{errorMessage}</Alert>}
                <ChecksTable/>
            </Box>
        </Container>
    );


}

export default Checks;