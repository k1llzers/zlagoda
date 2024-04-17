import * as React from 'react';
import TableContainer from "@mui/material/TableContainer";
import Card from "@mui/material/Card";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import StyledTableRow from "../styledComponent/styledTableRow";
import StyledTableCell from "../styledComponent/styledTableCell";
import {Box, Button, Dialog, DialogContent, FormControl, IconButton, MenuItem, Stack, Typography} from "@mui/material";
import TableBody from "@mui/material/TableBody";
import {useEffect, useState} from "react";
import {useAuth} from "../provider/authProvider";
import axios from "axios";
import TableRow from "@mui/material/TableRow";
import ModeEditIcon from "@mui/icons-material/ModeEdit";
import DeleteOutlineOutlinedIcon from "@mui/icons-material/DeleteOutlineOutlined";
import StyledTextField from "../styledComponent/styledTextField";
import StyledButton from "../styledComponent/styldButton";
import {Container} from "react-bootstrap";
import AddIcon from "@mui/icons-material/Add";
import Alert from "@mui/material/Alert";

const Categories = () => {
    const [categories, setCategories] = useState([])
    const {role} = useAuth()
    const [errorMessage, setErrorMessage] = useState("")
    const [openForm, setOpenForm] = useState(false)
    const [updateRow, setUpdateRow] = useState(undefined)

    useEffect(() => {
        fetchCategoryData();
    }, []);

    const fetchCategoryData = async () => {
        const response = await axios.get("http://localhost:8080/api/category/order-by/name")
        setCategories(response.data);
    }

    function processCategories(category) {
        return {
            id: category.id,
            name: category.name,
        }
    }

    const columns = [
        {field: 'name', headerName: 'Category'},
    ];

    const rows = categories.map((category) => processCategories(category));


    const clear = e => {
        setErrorMessage("")
    };

    function handleUpdate(row) {
        setUpdateRow(row)
        handleOpenForm()
    }

    function handleOpenForm() {
        setOpenForm(true)
    }

    function handleClose(){
        setOpenForm(false)
        setUpdateRow(undefined)
    }

    const handleDelete = async (categoryId) => {
        const response = await axios.delete("http://localhost:8080/api/category/" + categoryId)
        if (response.data.error) {
            setErrorMessage("Can't delete category that has products")
            setTimeout(() => setErrorMessage(""), 3500)
        } else {
            if (response.data === true)
                setCategories(categories.filter(category => category.id !== categoryId));
        }
    }

    function CategoryForm(props) {
        const {onClose, open, row} = props;
        const [name, setName] = useState(row ? row.name : "")
        const [disableAdd, setDisableAdd] = useState(true)

        useEffect(() => {
            if(name.trim() && name.length<=50) {
                setDisableAdd(false)
            }else {
                setDisableAdd(true)
            }

        }, [name]);

        const handleAdd = async () => {
            handleClose()
            let response;
            if(!row) {
                response = await axios.post("http://localhost:8080/api/category", {
                    name: name,
                })
            } else {
                response = await axios.put("http://localhost:8080/api/category", {
                    id: row.id,
                    name: name,
                })
            }
            if (response.data.error) {
                setErrorMessage(response.data.error)
                setTimeout(() => setErrorMessage(""), 3500)
            } else {
                fetchCategoryData()
            }
        }

        return (
            <Dialog onClose={onClose} open={open}>
                <DialogContent>
                    <FormControl fullWidth>
                        <StyledTextField id="outlined-basic" label="Name" variant="outlined" value={name}
                                         required
                                         error={name.length > 50}
                                         helperText={name.length > 50 ? "Too long" : ""}
                                         onChange={(event) => {setName(event.target.value)}}/>
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

        return (
            <React.Fragment>
                <TableRow sx={{'& > *': {borderBottom: 'unset'}}}>
                    <StyledTableCell component="th" scope="row " align="left">{row.name}</StyledTableCell>
                    {role === "MANAGER" && <StyledTableCell align="right">
                        <Button onClick={() => handleUpdate(row)}>
                            <ModeEditIcon color='action'/>
                        </Button>
                        <Button onClick={() => handleDelete(row.id)}>
                            <DeleteOutlineOutlinedIcon color="error"/>
                        </Button>
                    </StyledTableCell>}
                </TableRow>
            </React.Fragment>
        );
    }
    function CategoriesTable() {
        return (
            <React.Fragment>
                <TableContainer component={Card} sx={{ maxWidth: 400, margin: '30px auto' }}>
                    <Table>
                        <TableHead>
                            <StyledTableRow>
                                {columns.map((column) => (
                                    <StyledTableCell key={column.field}>
                                        {column.headerName}
                                    </StyledTableCell>
                                ))}
                                <StyledTableCell align="right"/>
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
            <CategoryForm
                open={openForm}
                onClose={handleClose}
                row={updateRow}
            />
            <Box sx={{maxWidth: 400, margin: '0 auto'}}>
                <Stack direction='row' justifyContent='space-between'>
                    {role === 'MANAGER' &&
                        <StyledButton variant="outlined" startIcon={<AddIcon />} onClick={handleOpenForm}>
                            Add
                        </StyledButton>}
                </Stack>
                {errorMessage && <Alert style={{width: '40%', fontSize: '15px', position: 'fixed', right: '30%', top: '5%'}} severity="error" onClose={clear}>{errorMessage}</Alert>}
                <CategoriesTable/>
            </Box>
        </Container>
    )

}
export default Categories